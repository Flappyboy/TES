import request from '@/utils/request';

export async function queryFakeList(params) {
  return request('/api/fake_list', {
    params,
  });
}

export async function queryProject(params) {
  return request('/api/project',{
    params,
    // mode:"no-cors",
  });
}

export async function queryMaintain(params) {
  return request('/api/maintain',{
    params,
  });
}
export async function allArcSmellInfos(projectId) {
  const params = {
    projectId: projectId,
  };
  console.log("projectId")
  console.log(projectId)
  return request('/api/info/arcSmellInfo',{
    params,
  });
}

export async function queryArcSmell(id) {
  return request('/api/info/arcSmellInfo/'+id,{
  });
}
