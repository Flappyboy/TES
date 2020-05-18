import { Form, Input, Modal,  Upload,Button,Icon,message } from 'antd';
import React from 'react';

const FormItem = Form.Item;
const normFile = e => {
  console.log('Upload event:', e);
  if (Array.isArray(e)) {
    return e;
  }
  return e && e.fileList;
};
const fs = {
  name: 'file',
  action: 'http://localhost:8080/api/ydy/upload',
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


const CreateForm = props => {
  const { modalVisible, form, handleAdd, handleModalVisible } = props;

  const okHandle = () => {
    form.validateFields((err, fieldsValue) => {
      if (err) return;
      form.resetFields();
      handleAdd(fieldsValue);
    });
  };

  return (
    <Modal
      destroyOnClose
      title="新建规则"
      visible={modalVisible}
      onOk={okHandle}
      onCancel={() => handleModalVisible()}
    >
      <FormItem
        labelCol={{
          span: 5,
        }}
        wrapperCol={{
          span: 15,
        }}
        label="描述"
      >
        {form.getFieldDecorator('desc', {
          rules: [
            {
              required: true,
              message: '请输入至少五个字符的规则描述！',
              min: 5,
            },
          ],
        })(<Input placeholder="请输入" />)}
      </FormItem>
      <Form.Item label="数据文件">
        {form.getFieldDecorator('datapath', {
          valuePropName: 'fileList',
          getValueFromEvent: normFile,
        })(
          <Upload style={{marginLeft: 62}}{...fs}>
            <Button>
              <Icon type="upload" /> 上传数据文件
            </Button>
          </Upload>,
        )}
      </Form.Item>
    </Modal>
  );
};

export default Form.create()(CreateForm);
