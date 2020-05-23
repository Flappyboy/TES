import { Button, Card, Icon, List,Table, Tabs, Typography } from 'antd';

const { TabPane } = Tabs;

import React, { Component } from 'react';
import { PageHeaderWrapper } from '@ant-design/pro-layout';
import { connect } from 'dva';
// 引入 ECharts 主模块
import echarts from 'echarts';
/*// 引入柱状图
import  'echarts/lib/chart/bar';
// 引入提示框和标题组件
import 'echarts/lib/component/tooltip';
import 'echarts/lib/component/title';*/
import styles from './style.less';
import { queryMaintain } from './service';
import InfoSelectorModal from "@/pages/arcsmell/components/InfoSelectorModal";

const { Paragraph } = Typography;
const style ={
  color: "red"
}
const show = function (value, th) {
  if(value>th)
    return <span style={style}>{value}</span>;
  else
    return value
}
let hdnth = 0
let hdinth = 0


@connect(({ arcsmell, loading }) => ({
  arcsmell,
  loading: loading.models.arcsmell,
}))
class ArcSmell extends Component {
  state = {
    modalVisible: false,
    updateModalVisible: false,
  };
  componentDidMount() {

    const { dispatch } = this.props;
    dispatch({
      type: 'arcsmell/fetchInfos',
    });
    // this.show();
  }

  show() {
    queryMaintain(1)
      .then(function (response) {
      console.log(response);
    });
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('maintain-chart'));
    // 绘制图表
    myChart.setOption({
      xAxis: {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
      },
      yAxis: {
        type: 'value'
      },
      series: [{
        data: [820, 932, 901, 934, 1290, 1330, 1320],
        type: 'line'
      }]
    })
  }
  handleModalVisible = flag => {
    this.setState({
      modalVisible: !!flag,
    });
  };
  render() {

    const {
      arcsmell: { info},
    } = this.props;
    const parentMethods = {
      handleModalVisible: this.handleModalVisible,
    };
    const { modalVisible, updateModalVisible } = this.state;
    return (
      <PageHeaderWrapper>
        {/*<div id="maintain-chart" style={{width: '80%',height:400}}></div>*/}

          <Card bordered={false}
                title={
                  <>
                    <Button type="primary" onClick={() => this.handleModalVisible(true)}>
                      选择检测结果
                    </Button>
                    &nbsp; &nbsp;
                  </>
                } >

        <Tabs defaultActiveKey="1"
              // onChange={callback}
        >
          {
            Object.keys(info.resultMap).map((item, index) => {
              const columns = [
                {
                  title: 'Name',
                  dataIndex: 'microserviceName',
                  key: 'microserviceName',
                  render: text => <a>{text}</a>,
                  onCell: record=>{
                    return {
                      fontSize: 20,
                    }
                  }
                },
                {
                  title: 'HDN',
                  dataIndex: 'hdn',
                  key: 'hdn',
                  sorter: (a, b) => a.hdn - b.hdn,
                  render: value =>  show(value, info.resultMap[item].hdnTh),
                  onCell: record=>{
                    return {
                      fontSize: 20,
                    }
                  }
                },
                {
                  title: 'HDIN',
                  dataIndex: 'hdin',
                  key: 'hdin',
                  sorter: (a, b) => a.hdin - b.hdin,
                  render: value =>  show(value, info.resultMap[item].hdinTh),
                },
                {
                  title: 'CN',
                  dataIndex: 'cn',
                  key: 'cn',
                  sorter: (a, b) => a.cn - b.cn,
                  render: value =>  show(value, 0),
                },
                {
                  title: 'MVDN',
                  dataIndex: 'mvdn',
                  key: 'mvdn',
                  sorter: (a, b) => a.mvdn - b.mvdn,
                  render: value =>  show(value, 0),
                },
                {
                  title: 'MVFN',
                  dataIndex: 'mvfn',
                  key: 'mvfn',
                  sorter: (a, b) => a.mvfn - b.mvfn,
                  render: value =>  show(value, 0),
                },
              ];
              return <TabPane tab={"V"+(index+1)} key={index+1}>
                <Table columns={columns} dataSource={Object.values(info.resultMap[item].resultMap)} />
              </TabPane>
            })
          }
        </Tabs>
          </Card>
        <InfoSelectorModal {...parentMethods} modalVisible={modalVisible} />
      </PageHeaderWrapper>
    );
  }
}

export default ArcSmell;
