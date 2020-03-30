import {
  Button,
  Card,
  Col,
  DatePicker,
  Form,
  Icon,
  Upload,
  Input,
  Popover,
  Row,
  Select,
  TimePicker,
} from 'antd';
import { UploadOutlined } from '@ant-design/icons';
import React, { Component } from 'react';
import { PageHeaderWrapper, RouteContext } from '@ant-design/pro-layout';
import { connect } from 'dva';
import TableForm from './components/TableForm';
import FooterToolbar from './components/FooterToolbar';
import styles from './style.less';
import ActionSelectorModal from "@/pages/action/components/ActionSelectorModal";

const { Option } = Select;
const { RangePicker } = DatePicker;
const fieldLabels = {
  name: '仓库名',
  d: 'd',
  c: 'c',
  r: 'r',
  url: '仓库域名',
  owner: '仓库管理员',
  approver: '审批人',
  dateRange: '生效日期',
  type: '仓库类型',
  name2: '任务名',
  url2: '任务描述',
  owner2: '执行人',
  approver2: '责任人',
  dateRange2: '生效日期',
  type2: '任务类型',
};
const tableData = [
  {
    key: '1',
    workId: '00001',
    name: 'John Brown',
    department: 'New York No. 1 Lake Park',
  },
  {
    key: '2',
    workId: '00002',
    name: 'Jim Green',
    department: 'London No. 1 Lake Park',
  },
  {
    key: '3',
    workId: '00003',
    name: 'Joe Black',
    department: 'Sidney No. 1 Lake Park',
  },
];
const normFile = e => {
  console.log('Upload event:', e);
  if (Array.isArray(e)) {
    return e;
  }
  return e && e.fileList;
};
const fs = {
  name: 'file',
  action: '/api/upload',
  headers: {
    authorization: 'authorization-text',
  },
  onChange(info) {
    if (info.file.status !== 'uploading') {
      console.log(info.file, info.fileList);
    }
    if (info.file.status === 'done') {
      message.success(`${info.file.name} file uploaded successfully`);
    } else if (info.file.status === 'error') {
      message.error(`${info.file.name} file upload failed.`);
    }
  },
};
@connect(({ action, loading }) => ({
  action: action,
  loading: loading.models.action,
}))
class AdvancedForm extends Component {
  state = {
    modalVisible: false,
    updateModalVisible: false,
  };
  getErrorInfo = () => {
    const {
      form: { getFieldsError },
    } = this.props;
    const errors = getFieldsError();
    const errorCount = Object.keys(errors).filter(key => errors[key]).length;

    if (!errors || errorCount === 0) {
      return null;
    }

    const scrollToField = fieldKey => {
      const labelNode = document.querySelector(`label[for="${fieldKey}"]`);

      if (labelNode) {
        labelNode.scrollIntoView(true);
      }
    };

    const errorList = Object.keys(errors).map(key => {
      if (!errors[key]) {
        return null;
      }

      const errorMessage = errors[key] || [];
      return (
        <li key={key} className={styles.errorListItem} onClick={() => scrollToField(key)}>
          <Icon type="cross-circle-o" className={styles.errorIcon} />
          <div className={styles.errorMessage}>{errorMessage[0]}</div>
          <div className={styles.errorField}>{fieldLabels[key]}</div>
        </li>
      );
    });
    return (
      <span className={styles.errorIcon}>
        <Popover
          title="表单校验信息"
          content={errorList}
          overlayClassName={styles.errorPopover}
          trigger="click"
          getPopupContainer={trigger => {
            if (trigger && trigger.parentNode) {
              return trigger.parentNode;
            }

            return trigger;
          }}
        >
          <Icon type="exclamation-circle" />
        </Popover>
        {errorCount}
      </span>
    );
  };

  validate = () => {
    const {
      form: { validateFieldsAndScroll },
      dispatch,
    } = this.props;
    validateFieldsAndScroll((error, values) => {
      if (!error) {
        // submit the values
        dispatch({
          type: 'formAndadvancedForm/submitAdvancedForm',
          payload: values,
        });
      }
    });
  };
  handleModalVisible = flag => {
    this.setState({
      modalVisible: !!flag,
    });
  };

  render() {
    const {
      form: { getFieldDecorator },
      submitting,
    } = this.props;
    const parentMethods = {
      handleModalVisible: this.handleModalVisible,
    };
    const { modalVisible, updateModalVisible } = this.state;
    return (
      <>
        <PageHeaderWrapper content="执行操作">
          <Card title={
            <>
              {this.props.action.name}
            <Button type="primary" onClick={() => this.handleModalVisible(true)}>
              选择操作
            </Button>
              &nbsp; &nbsp; 导入数据并检测架构异味
            </>
          } className={styles.card} bordered={false}>
            <Form layout="inline" hideRequiredMark>
              <Row gutter={16}>
                <Col lg={10} md={12} sm={24}>
                  {/*<Upload
                    name='file'
                  action='https://www.mocky.io/v2/5cc8019d300000980a055e76'
                  // headers={authorization:'authorization-text'}
                  >
                    <Button>
                      <UploadOutlined /> Click to Upload
                    </Button>
                  </Upload>*/}
                  <Form.Item label="数据文件">
                    {getFieldDecorator('upload', {
                      valuePropName: 'fileList',
                      getValueFromEvent: this.normFile,
                    })(
                      <Upload style={{marginLeft: 62}}{...fs}>
                        <Button>
                          <Icon type="upload" /> 上传压缩后的数据文件
                        </Button>
                      </Upload>,
                    )}
                  </Form.Item>
                </Col>
              </Row>
              <Row style={{marginTop : 10}} gutter={16}>
                <Col style={{marginTop : 9}}lg={2} md={12} sm={24}>
                  MV检测参数
                </Col>
                <Col lg={5} md={12} sm={24}>
                   <Form.Item label='临近提交距离'>
                    {getFieldDecorator('d', {
                      rules: [
                        {
                          required: true,
                          message: '',
                        },
                      ],
                    })(<Input placeholder="" />)}
                  </Form.Item>
                </Col>
                <Col lg={5} md={12} sm={24}>
                  <Form.Item label='最少提交次数'>
                    {getFieldDecorator('c', {
                      rules: [
                        {
                          required: true,
                          message: '',
                        },
                      ],
                    })(<Input placeholder="" />)}
                  </Form.Item>
                </Col>
                <Col lg={6} md={12} sm={24}>
                  <Form.Item label='最小同时出现比例'>
                    {getFieldDecorator('r', {
                      rules: [
                        {
                          required: true,
                          message: '',
                        },
                      ],
                    })(<Input placeholder="" />)}
                  </Form.Item>
                </Col>
              </Row>
          <Row style={{marginTop : 20}}>
            <Col lg={3} md={12} sm={24}>
              <Button type="primary" onClick={this.validate} loading={submitting}>
                执行
              </Button>
            </Col>
          </Row>
            </Form>
          </Card>
          <ActionSelectorModal {...parentMethods} modalVisible={modalVisible} />
        </PageHeaderWrapper>
        <RouteContext.Consumer>
          {({ isMobile }) => (
            <FooterToolbar isMobile={isMobile}>
              {this.getErrorInfo()}
              <Button type="primary" onClick={this.validate} loading={submitting}>
                执行
              </Button>
            </FooterToolbar>
          )}
        </RouteContext.Consumer>
      </>
    );
  }
}

export default Form.create()(AdvancedForm);
// export default AdvancedForm);
