export default {
  // 支持值为 Object 和 Array
  'GET /api/task': {
    "result": [
      {
        "id": 392858379259742208,
        "createdTime": 1575215971269,
        "updatedTime": 1575215971269,
        "inputInfos": {},
        "action": {
          "name": "GitCommit",
          "desc": "git commit",
          "inputMeta": {
            "fieldList": [
              {
                "name": "local_repo_dir",
                "inputClass": "top.jach.tes.core.api.domain.info.InfoProfile"
              },
              {
                "name": "specific_commits",
                "inputClass": "top.jach.tes.core.api.domain.info.InfoProfile"
              },
              {
                "name": "repo_name",
                "inputClass": "java.lang.String"
              },
              {
                "name": "repos_id",
                "inputClass": "java.lang.Long"
              }
            ]
          }
        },
        "status": "0",
        "project": {
          "id": 392858379068901376,
          "createdTime": 1575215971178,
          "updatedTime": 1575215971178,
          "name": "tes1",
          "desc": "just test"
        },
        "context": null
      }
    ],
    "pageNum": 0,
    "pageSize": 10,
    "total": 1,
  },
};
