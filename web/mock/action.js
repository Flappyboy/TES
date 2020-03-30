export default {
  // 支持值为 Object 和 Array
  'GET /api/action': {
    "result": [
      {
        "name": "ImportData",
        "action": "ImportDataAction",
        "desc": "导入数据",
        "meta": [
          {
            "name": "file",
            "displayName": "File",
            "inputClass": "file",
          }
        ],
      },
    ],
    "pageNum": 0,
    "pageSize": 10,
    "total": 1,
  },

  'POST /api/upload': {
    "name": "xxx.zip",
    "status": "done",
    "url": "https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.zip",
    "thumbUrl": "https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.zip"
  },
};
