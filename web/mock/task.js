const tasks = {
  '1': [
    {
      "id": 1,
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
    },
  ],
  '2': [
    {
      "id": 2,
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
}
export default {
  'GET /api/task': (req, res) => {
    let i = req.query.projectId;
    res.json({
      result: tasks[i],
      pageSize: 10,
      pageNum: 1,
      total: tasks[i].length,
    });
  },
};
