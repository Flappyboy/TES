const infoTypes = {
  '1': [
    {
      "className": "top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo",
      "infoName": "Microservice"
    },
    {
      "className": "top.jach.tes.plugin.tes.code.go.GoPackagesInfo",
      "infoName": "GoAstPackage"
    },
    {
      "className": "top.jach.tes.plugin.tes.code.git.tree.TreesInfo",
      "infoName": "GitTreeInfo"
    },
    {
      "className": "top.jach.tes.core.impl.domain.element.ElementsValue",
      "infoName": null
    },
    {
      "className": "top.jach.tes.plugin.tes.code.repo.ReposInfo",
      "infoName": "TES_INPUT_INFO"
    },
    {
      "className": "top.jach.tes.plugin.jhkt.dts.DtssInfo",
      "infoName": "BugDts"
    },
    {
      "className": "top.jach.tes.core.impl.domain.info.value.FileInfo",
      "infoName": "TES_INPUT_INFO"
    },
    {
      "className": "top.jach.tes.plugin.tes.code.git.version.VersionsInfo",
      "infoName": "VersionsForMaster"
    },
    {
      "className": "top.jach.tes.plugin.tes.code.repo.ReposInfo",
      "infoName": "TargetSystem"
    },
    {
      "className": "top.jach.tes.core.impl.domain.relation.PairRelationsInfo",
      "infoName": "RelationBugAndMicroservice"
    },
    {
      "className": "top.jach.tes.core.impl.domain.info.value.StringInfo",
      "infoName": null
    },
    {
      "className": "top.jach.tes.plugin.tes.code.git.gitlab.MergeRequestsInfo",
      "infoName": "MergeRequestForRepo"
    },
    {
      "className": "top.jach.tes.core.impl.domain.info.value.StringInfo",
      "infoName": "TES_INPUT_INFO"
    },
    {
      "className": "top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo",
      "infoName": "TES_INPUT_INFO"
    },
    {
      "className": "top.jach.tes.plugin.tes.code.git.version.VersionsInfo",
      "infoName": "VersionsForRelease"
    },
    {
      "className": "top.jach.tes.core.impl.domain.relation.PairRelationsInfo",
      "infoName": "TES_INPUT_INFO"
    },
    {
      "className": "top.jach.tes.core.impl.domain.info.InfoOfInfo",
      "infoName": "TES_IMPORT_DATA"
    },
    {
      "className": "top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo",
      "infoName": "MicroserviceForRepos"
    },
    {
      "className": "top.jach.tes.core.impl.domain.relation.PairRelationsInfo",
      "infoName": "MicroserviceCallRelation"
    },
    {
      "className": "top.jach.tes.core.impl.domain.info.value.LongInfo",
      "infoName": "TES_INPUT_INFO"
    }
  ],
  '2': [
    {},
  ],
}
export default {
  'GET /api/info/allTypes': (req, res) => {
    let i = req.query.projectId;
    res.json({
      result: infoTypes[i],
      total: infoTypes[i].length,
    });
  },
  'GET /api/info': (req, res) => {
    const projectId = req.query.projectId;
    const infoClass = req.query.className;
    const infoName = req.query.infoName;
    const pageNum = req.query.pageNum;
    const pageSize = req.query.pageSize;
    let result = [];
    for (let i = 0; i < pageSize; i++) {
      result.push({
        id: i*3 + pageNum*pageSize + 29058743234,
        infoClass: infoClass,
        name: infoName,
      })
    }
    res.json({
      result: result,
      pageNum: pageNum,
      pageSize: pageSize,
      total: 30,
    });
  },
};
