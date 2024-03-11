import axios from '@axios'
import { defineStore } from 'pinia'

export const useInvoiceStore = defineStore('InvoiceStore', {
  actions: {
    fetchInvoices() {
      return axios.get('/invoices')
    },
    fetchInvoice(invoiceId) {
      return axios.get('/invoice', { params: { invoiceId } })
    },
    deleteInvoice(invoiceId) {
      return axios.get('/invoice/delete', { params: { invoiceId } })
    },
  },
})
