# 视频上传完整流程

## 第一阶段：分片上传

```
① 前端选中文件
   └── 弹出上传弹窗，展示文件名、大小
```

```
② POST /api/upload/init
   Body: { file_name, file_size }
   └── 创建 tb_upload_task（status=0上传中）
   └── 返回 { taskId, chunkSize, totalChunks }
```

```
③ 前端收到 taskId
   ├── 将文件按 chunkSize 切成 N 片
   ├── 每片按顺序上传（或并发 3-5 片）
   └── POST /api/upload/{taskId}/chunk?index=0  Body: <二进制>
```

```
④ 后端收到分片
   ├── MinIO uploadPart(taskId, index, chunkData)
   ├── tb_upload_task.completed_chunks + 1
   └── 返回成功
```

```
⑤ 所有分片上传完毕
   └── POST /api/upload/{taskId}/merge
```

```
⑥ 后端合并
   ├── MinIO completeMultipartUpload → 得到原始文件
   ├── tb_upload_task.status = 1(上传完成)
   ├── 创建 tb_video（status=0待审核）
   ├── 创建 tb_video_episode（第1集，transcode_status=0待处理）
   └── 返回 { videoId }
```

前端进度：每上传完一片 → 进度条 = completed_chunks / total_chunks × 100%

断点续传：刷新页面后调 GET /api/upload/{taskId} 恢复进度

---

## 第二阶段：异步转码

```
⑦ 合并完成后，后端异步提交转码任务

  TranscodeService.startTranscode(episodeId)
       │
       ▼  异步线程池
  ┌──────────────────────────────────────────────┐
  │                                              │
  │  1. 下载原始文件 → /tmp/                      │
  │     progress=5 (存入 Redis)                   │
  │                                              │
  │  2. ffprobe 检测 HDR                         │
  │     ├── HDR → 生成 5 条 stream               │
  │     │         (480p/720p/1080p/4K SDR/4K HDR)│
  │     └── SDR → 生成 3 条 stream               │
  │               (480p/720p/1080p)               │
  │     tb_video_episode.transcode_status=1       │
  │     progress=10                               │
  │                                              │
  │  3. 依次转码各清晰度 HLS                      │
  │     每完成一个清晰度 → 上传到 MinIO           │
  │     创建 tb_video_stream                      │
  │     progress=10→90                            │
  │                                              │
  │  4. 完成                                      │
  │     tb_video_episode.transcode_status=2       │
  │     progress=100 (Redis, TTL 1h)               │
  │  5. 清理 /tmp 临时文件                         │
  └──────────────────────────────────────────────┘
```

---

## 第三阶段：审核发布

```
⑧ 管理员审核
  PUT /admin/video/{videoId}/status?status=1
       ├── tb_video.status = 1(已发布)
       └── 前台可见
```

---

## 第四阶段：用户播放

```
⑨ 前端打开视频页
  GET /api/video/{videoId}
       └── 返回 tb_video + tb_video_episode + tb_video_stream 列表

  前端 hls.js 加载默认清晰度（720P）的 m3u8
  用户可切换 480P / 720P / 1080P / 4K HDR
```

---

## 接口一览

| 阶段 | 方法 | 路径 | 说明 |
|---|---|---|---|
| ① | POST | /api/upload/init | 初始化上传任务 |
| ② | POST | /api/upload/{taskId}/chunk?index=N | 上传分片 |
| ③ | POST | /api/upload/{taskId}/merge | 合并分片 |
| ④ | GET | /api/upload/{taskId} | 查询任务状态（续传用） |
| ⑤ | POST | /api/video | 创建投稿（标题/封面/分类） |
| ⑥ | GET | /api/video/episode/{episodeId} | 轮询转码进度 |
| ⑦ | GET | /api/video/{videoId} | 播放页详情 |
