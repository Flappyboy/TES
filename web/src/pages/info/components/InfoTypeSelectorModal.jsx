import { Form, Input, Modal } from 'antd';
import React from 'react';
import InfoTypeSelector from "@/pages/info/components/InfoTypeSelector";

const FormItem = Form.Item;

const InfoTypeSelectorModal = props => {
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
      // destroyOnClose
      title="选择Info类型"
      visible={modalVisible}
      width={1000}
      onOk={okHandle}
      onCancel={() => handleModalVisible()}
    >
    <InfoTypeSelector />
    </Modal>
  );
};

export default Form.create()(InfoTypeSelectorModal);
