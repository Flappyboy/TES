import { queryFakeList, queryProject, addProject,delProject } from './service';

const Model = {
  namespace: 'projects',
  state: {
    list: [],
  },
  effects: {
    *fetchList({ payload }, { call, put }) {
      const response = yield call(queryProject, payload);
      /*console.log("response:");
      console.log(response);*/
      const list = response.result;
      yield put({
        type: 'queryList',
        payload: Array.isArray(list) ? list : [],
      });
    },
    *fetch({ payload }, { call, put }) {
      const response = yield call(queryFakeList, payload);
      yield put({
        type: 'queryList',
        payload: Array.isArray(response) ? response : [],
      });
    },
    *add({ payload, callback }, { call, put }) {
      const response = yield call(addProject, payload);
      callback(response)
    },
    *del({ payload, callback }, { call, put }) {
      const response = yield call(delProject, payload);
      callback(response)
    },
    *chooseProject({payload}, {put}){
      yield put({
        type: 'project/changeProject',
        payload: payload,
      });
    }
  },
  reducers: {
    queryList(state, action) {
      return { ...state, list: action.payload };
    },
  },
};
export default Model;
