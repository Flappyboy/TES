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

const columns = [
  {
    title: 'Name',
    dataIndex: 'microservice',
    key: 'microservice',
    render: text => <a>{text}</a>,
  },
  {
    title: 'HDN',
    dataIndex: 'hublikes',
    key: 'hublikes',
    sorter: (a, b) => a.hublikes - b.hublikes,
    render: value =>  show(value, 7.77),
  },
  {
    title: 'HDIN',
    dataIndex: 'hublikeWithWeight',
    key: 'hublikeWithWeight',
    sorter: (a, b) => a.hublikeWithWeight - b.hublikeWithWeight,
    render: value =>  show(value, 73.45),
  },
  {
    title: 'CN',
    dataIndex: 'cyclic',
    key: 'cyclic',
    sorter: (a, b) => a.cyclic - b.cyclic,
    render: value =>  show(value, 0),
  },
  {
    title: 'MVDN',
    dataIndex: 'MVDN',
    key: 'MVDN',
    sorter: (a, b) => a.MVDN - b.MVDN,
    render: value =>  show(value, 0),
  },
  {
    title: 'MVFN',
    dataIndex: 'MVFN',
    key: 'MVFN',
    sorter: (a, b) => a.MVFN - b.MVFN,
    render: value =>  show(value, 0),
  },
  /*{
    title: 'Tags',
    key: 'tags',
    dataIndex: 'tags',
    render: tags => (
      <span>
        {tags.map(tag => {
          let color = tag.length > 5 ? 'geekblue' : 'green';
          if (tag === 'loser') {
            color = 'volcano';
          }
          return (
            <Tag color={color} key={tag}>
              {tag.toUpperCase()}
            </Tag>
          );
        })}
      </span>
    ),
  },
  {
    title: 'Action',
    key: 'action',
    render: (text, record) => (
      <span>
        <a style={{ marginRight: 16 }}>Invite {record.name}</a>
        <a>Delete</a>
      </span>
    ),
  },*/
];

@connect(({ arcsmell, loading }) => ({
  arcsmell,
  loading: loading.models.arcsmell,
}))
class ArcSmell extends Component {
  componentDidMount() {

    const { dispatch } = this.props;
    dispatch({
      type: 'arcsmell/fetchList',
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
  render() {
    const {
      arcsmell: { list },
    } = this.props;
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
            list.map((item, index) => {
              return <TabPane tab={"V"+(index+1)} key={index+1}>
                <Table columns={columns} dataSource={item.ms} />
              </TabPane>
            })
          }
        </Tabs>
          </Card>
      </PageHeaderWrapper>
    );
  }
}

export default ArcSmell;
