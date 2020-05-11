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
export async function queryArcSmell(params) {
  return request('/api/arcsmell',{
    params,
  });
}
export async function allArcSmellInfos(params) {
  return request('/api/arcsmells',{
    params,
  });
}
