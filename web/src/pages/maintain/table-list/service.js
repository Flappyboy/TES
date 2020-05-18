import request from '@/utils/request';

export async function queryDataset(params) {
  return request('/api/ydy/dataset', {
    params,
  });
}
export async function addDataset(params) {
  return request('/api/ydy/dataset', {
    method: 'POST',
    params,
  });
}
export async function delDataset(params) {
  return request('/api/ydy/dataset/delete', {
    method: 'POST',
    data: params,
  });
}

export async function getMetricsInfo(params) {
  return request('http://localhost:8080/yangdeyu/MetricsInfo/getInfo', {
    params,
  });
}
