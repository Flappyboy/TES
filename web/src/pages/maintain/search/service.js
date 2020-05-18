import request from '@/utils/request';

export async function queryFakeList(params) {
  return request('/api/maintain/fake_list', {
    params,
  });
}

export async function queryProject(params) {
  return request('/api/maintain/project', {
    params,
    // mode:"no-cors",
  });
}

export async function queryMaintain(params) {
  return request('/api/ydy/calculate/'+params.id, {
    // params,
  });
}
