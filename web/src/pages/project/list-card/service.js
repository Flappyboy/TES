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
export async function addProject(params) {
  return request('/api/project',{
    method: 'POST',
    params,
    // mode:"no-cors",
  });
}
export async function delProject(params) {
  return request('/api/project',{
    method: 'DELETE',
    params,
    // mode:"no-cors",
  });
}

