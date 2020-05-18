import { Form, Row, Col, Select, Input, Button, Card, Icon, List, Typography, Table, Tabs } from 'antd';
const { TabPane } = Tabs;
import React, { Component } from 'react';
// import React, { Component, Fragment } from 'react';
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
import Link from '../../dashboard/workplace';
import SearchForm from "@/pages/maintain/search/components/SearchForm";
import MetricsForm from "@/pages/maintain/search/components/MetricsForm";

const { Paragraph } = Typography;
const FormItem = Form.Item;
var { servicenames } = [];
@connect(({ search, loading }) => ({
  maintain:search,
  loading: loading.models.search,
}))
class Maintain extends Component {
  state = {
    search:{
      versions: [],
      microservices: [],
    },
    metrics:{
      microservices: [],
      metrics: ["packageCout"],
    },
    searchResult:[],
    metricResult:[]
  };
  myChart = null;
  componentDidMount() {
  // UNSAFE_componentWillReceiveProps(){
    console.log(this.props.location.query.id)
    const {
      dispatch,
    } = this.props;
    dispatch({
      type: 'search/fetch',
      payload: {id: this.props.location.query.id},
      callback: (response) => {
        console.log("完成 Complete");
        console.log(response.length);
        this.data = response;
        let vs = []
        this.data.map(d => {
          vs.push(d.version)
        });

        let ms = {};
        for(let index in this.data){
          const d = this.data[index]
          console.log(d)
            const mim = d['microservicesInfo']['microservices']
            for (let mi in mim){
              const m = mim[mi]['elementName']
              ms[m]=1
            }
        }
        let mskeys = [];
        for (let key in ms){
          mskeys.push(key)
        }
        console.log(mskeys)
        console.log(ms)
        this.setState({
          search:{versions: vs, microservices: []},
          metrics:{
            microservices: mskeys,
            metrics: this.state.metrics.metrics
          }
        })
      }
    });
    // this.show();
  }
  show(data, links) {
    if(this.myChart==null) {
      this.myChart = echarts.init(document.getElementById('maintain-chart'));
    }
    var categories = [];
    for (var i = 0; i < 5; i++) {
      categories[i] = {
        name: '包名' + i,
      };
    }
    var option = {
      // legend:[{
      //   data: categories.map(function (a) {
      //     return a.name;
      //   })
      // }],
      series: [
        {
          type: 'graph',
          layout: 'force',
          symbol: 'circle',
          symbolSize: 80,
          roam: true,
          edgeSymbol: ['circle', 'arrow'],
          edgeSymbolSize: [2, 10],
          label: {
            normal: {
              show: true,
              textStyle: {
                fontSize: 12,
              },
            },
          },
          force: {
            repulsion: 2500,
            edgeLength: [10, 50],
          },
          draggable: true,
          lineStyle: {
            normal: {
              opacity: 1,
              width: 2,
              curveness: 0.2,
            },
          },

          data: response.nodes,
          links: response.relations,
          categories: categories,
        },
      ],
    };
    // 绘制图表
    this.myChart.setOption(option);
  }

  selectVersion = value => {
    let ms = []
    const data = this.props.maintain.data;
    // console.log(data)
    // console.log(value)
    for(let index in data){
      const d = data[index]
      console.log(d)
      if (d.version == value){
        for (let i in d.metricsList){
          ms.push(d.metricsList[i].elementName)
        }
        break;
      }
    }
    console.log(ms)
    this.setState({
      search:{
        versions: this.state.search.versions,
        microservices: ms}
    })
  };

  handleSearch = values => {
       let vn =  values.version_name
       let mn =  values.microservice_name
        // echarts
        console.log(this.data);
       //this.show(data, links);
        //
        const data = this.props.maintain.data;
        for(let index in data){
          const d = data[index]
          console.log(d)
          if (d.version == vn){
            for (let i in d.metricsList){
              const m = d.metricsList[i];
              if(m.elementName == mn){
                let sr = []
                sr.push({
                  name: "n",
                  value: m.packageCout
                });
                sr.push({
                  name: "m",
                  value: m.fileCount
                });
                this.setState({
                  searchResult: sr,
                })
              }
            }
            break;
          }
        }
  };

  handleSearchMetric = values => {

    let mn =  values.microservice_name
    let men =  values.metric_name
    // echarts
    console.log(this.data);
    //this.show(data, links);
    //
    const data = this.props.maintain.data;
    let mr = []
    for(let index in data){
      const d = data[index]
        for (let i in d.metricsList){
          const m = d.metricsList[i];
          if(m.elementName == mn){
            mr.push({
              name: d.version,
              value: m[men]
            });
            break;
          }
        }
    }
    this.setState({
      metricResult: mr,
    })
  };

  renderForm() {
    return (
      <SearchForm searchBack={this.handleSearch.bind(this)} selectVersion={this.selectVersion.bind(this)} search={this.state.search}/>
    );
  }
  renderFormMetrics() {
    return (
      <MetricsForm searchBack={this.handleSearchMetric.bind(this)} metrics={this.state.metrics}/>
    );
  }
  tabcallback(){

  }

  render() {
    if(this.props.loading){
      return (<>加载中</>)
    }
    let searchResult = this.state.searchResult;
    let metricResult =this.state.metricResult;
    const columnsSearch = [
      {
        title: '指标名称Search',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: '值',
        dataIndex: 'value',
        key: 'value',
      },
    ];

    const columnsMetrics = [
      {
        title: '指标名称Metrics',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: '值',
        dataIndex: 'value',
        key: 'value',
      },
    ];

    return (
      <PageHeaderWrapper>
        <Tabs defaultActiveKey="1" onChange={this.tabcallback}>
          <TabPane tab="Tab 1" key="1">
            <div className={styles.tableListForm}>{this.renderForm()}</div>
            <Row gutter={24}>
              <Col xl={16} lg={24} md={24} sm={24} xs={24}>
                <Card
                  style={{
                    marginBottom: 24,
                  }}
                  title="微服务内模块的依赖关系"
                  bordered={false}
                  bodyStyle={{
                    padding: 0,
                  }}
                >
                  <div id="maintain-chart" style={{ width: '100%', height: 650 }}>
                    {' '}
                  </div>
                </Card>
              </Col>
              <Col xl={8} lg={24} md={24} sm={24} xs={24}>
                <Card
                  style={{
                    marginBottom: 24,
                  }}
                  title="可维护性指标"
                  bordered={false}
                  bodyStyle={{
                    padding: 0,
                  }}
                >
                  <Table dataSource={searchResult} columns={columnsSearch} />;
                </Card>
              </Col>
            </Row>
          </TabPane>
          <TabPane tab="Tab 2" key="2">
            <div className={styles.tableListForm}>{this.renderFormMetrics()}</div>
            <Row gutter={24}>
              <Col xl={16} lg={24} md={24} sm={24} xs={24}>
                <Card
                  style={{
                    marginBottom: 24,
                  }}
                  title="可维护性指标折线图"
                  bordered={false}
                  bodyStyle={{
                    padding: 0,
                  }}
                >
                  <div id="maintain-chart" style={{ width: '100%', height: 650 }}>
                    {' '}
                  </div>
                </Card>
              </Col>
              <Col xl={8} lg={24} md={24} sm={24} xs={24}>
                <Card
                  style={{
                    marginBottom: 24,
                  }}
                  title="指标展示"
                  bordered={false}
                  bodyStyle={{
                    padding: 0,
                  }}
                >
                  <Table dataSource={metricResult} columns={columnsMetrics} />;
                </Card>
              </Col>
            </Row>
          </TabPane>
        </Tabs>,



        {/*<div id="maintain-chart" style={{width: '30%',height:400}}> </div>*/}
      </PageHeaderWrapper>
    );
  }
}

// export default Maintain;
export default Maintain;
