import {queryArcSmell, queryFakeList, queryProject} from './service';
import {allArcSmellInfos} from "@/pages/arcsmell/service";

const Model = {
  namespace: 'arcsmell',
  state: {
    info: {resultMap:{}},
    infos:[],
  },
  effects: {
    *fetchArcSmell({ payload }, { call, put }) {
      const response = yield call(queryArcSmell, payload.id);
      /*console.log("response:");
      console.log(response);*/
      yield put({
        type: 'saveInfo',
        payload: response,
      });
    },
    *fetchInfos({ payload }, { call,put, select}){
      console.log('fetchInfos')
      const projectId = yield select(
        state => state.project.currentProject.id
      );
      const response = yield call(allArcSmellInfos, projectId);
      yield put({
        type: 'saveAllArcsmellInfos',
        payload: {
          list: response,
          pagination: {
            pageSize: 5,
          },
        },
      });
    },
    *fetch({ payload }, { call, put }) {
      const response = yield call(queryFakeList, payload);
      yield put({
        type: 'queryList',
        payload: Array.isArray(response) ? response : [],
      });
    },
    *chooseProject({payload}, {put}){
      yield put({
        type: 'project/changeProject',
        payload: payload,
      });
    }
  },
  reducers: {
    saveInfo(state, action) {
      return { ...state, info: action.payload };
    },
    saveAllArcsmellInfos(state, action) {
      return { ...state, infos: action.payload };
    },
  },
};
export default Model;
