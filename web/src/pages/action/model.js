import { message } from 'antd';
import { fakeSubmitForm, allActions,im } from './service';
import {queryAllInfoTypes} from "@/pages/info/service";
import router from 'umi/router';

const Model = {
  namespace: 'action',
  state: {
    actions:[],
    action: {
      "name": "ImportData",
      "action": "ImportDataAction",
      "desc": "导入数据",
      "meta": [
        {
          "name": "file",
          "displayName": "File",
          "inputClass": "file",
        }
      ],
    },
    loading: false,
  },
  effects: {
    *execute({ payload }, { call, select,put }){
      yield put({
        type: 'loading',
        payload: {loading:true},
      });
      console.log("execute")
      payload.projectId = yield select(
        state => state.project.currentProject.id
      );
      const response = yield call(im, payload);
      console.log(response)
      let r = yield put({
        type: 'loading',
        payload: {loading:false},
      });
      console.log(r)
      router.push(`/arcsmell`);
    },
    *fetchActions({ payload }, { call, put }){
      console.log('fetchActionssss');
      const response = yield call(allActions, payload);
      yield put({
        type: 'saveActions',
        payload: {
          list: response.result,
          pagination: {
            pageSize: 5,
          },
        },
      });
    },
    *chooseAction({ payload }, { call }){

    }
  },
  reducers: {
    saveActions(state, action) {
      return {...state, actions: action.payload};
    },
    loading(state, payload){
      return {...state, loading: payload.loading};
    }
  }
};
export default Model;
