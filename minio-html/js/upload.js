//分片大小 5m
const chunkSize = 5 * 1024 * 1024;

uploadFile = async () => {
    //获取用户选择的文件
    const file = document.getElementById("upload").files[0];
    //文件大小(大于5m再分片哦，否则直接走普通文件上传的逻辑就可以了，这里只实现分片上传逻辑)
    const fileSize = file.size

    if (fileSize <= chunkSize) {
        console.log("上传的文件大于5m才能分片上传")
        //小于5M直接上传文件
    }

    //计算当前选择文件需要的分片数量
    const chunkCount = Math.ceil(fileSize / chunkSize)
    console.log("文件大小：", (file.size / 1024 / 1024) + "Mb", "分片数：", chunkCount)

    //获取文件md5
    const fileMd5 = await getFileMd5(file);
    console.log("文件md5：", fileMd5)

    console.log("向后端请求本次分片上传初始化")
    //向后端请求本次分片上传初始化
    const initUploadParams = JSON.stringify({ chunkCount: chunkCount, fileMd5: fileMd5, fileName: file.name })
    $.ajax({
        url: "http://127.0.0.1:8001/file/initChunk", type: 'POST', contentType: "application/json", processData: false, data: initUploadParams,
        success: async function (res) {
            //code = 0 文件在之前已经上传完成，直接走秒传逻辑；code = 1 文件上传过，但未完成，走续传逻辑;code = 200 则仅需要合并文件
            if (res.data.code === 200) {
                console.log("当前文件上传情况：所有分片已在之前上传完成，仅需合并")
                composeFile(fileMd5, file.name)
                return;
            }
            if (res.data.code === 0) {
                console.log("当前文件上传情况：秒传")
                videoPlay(res.data.uploadUrl)
                return
            }
            console.log("当前文件上传情况：初次上传 或 断点续传")
            const chunkUploadUrls = res.data.chunkUploadUrls

            for (item of chunkUploadUrls) {
                //分片开始位置
                let start = (item.partNumber - 1) * chunkSize
                //分片结束位置
                let end = Math.min(fileSize, start + chunkSize)
                //取文件指定范围内的byte，从而得到分片数据
                let _chunkFile = file.slice(start, end)
                console.log("开始上传第" + item.partNumber + "个分片")
                await $.ajax({
                    url: item.uploadUrl, type: 'PUT', contentType: false, processData: false, data: _chunkFile,
                    success: function (res) {
                        console.log("第" + item.partNumber + "个分片上传完成")
                    }
                })
            }
            //请求后端合并文件
            composeFile(fileMd5, file.name)
        }
    })
}
/**
 * 请求后端合并文件
 * @param fileMd5
 * @param fileName
 */
composeFile = (fileMd5, fileName) => {
    console.log("开始请求后端合并文件")
    const composeParams = JSON.stringify({ fileMd5: fileMd5, fileName: fileName })
    $.ajax({
        url: "http://127.0.0.1:8001/file/composeFile", type: 'post', contentType: "application/json", processData: false, data: composeParams,
        success: function (res) {
            console.log("合并文件完成", res.data)
            videoPlay(res.data.uploadUrl)
        }
    })
}
/**
 * 测试视频播放
 * @param url
 */
videoPlay = (url) => {
    let video = document.getElementById("video")
    video.src = url
    video.load()
}
/**
 * 获取文件MD5
 * @param file
 * @returns {Promise<unknown>}
 */
getFileMd5 = (file) => {
    let fileReader = new FileReader()
    fileReader.readAsBinaryString(file)
    let spark = new SparkMD5()
    return new Promise((resolve) => {
        fileReader.onload = (e) => {
            spark.appendBinary(e.target.result)
            resolve(spark.end())
        }
    })
}
