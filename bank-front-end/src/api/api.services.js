import { object } from "yup";
import api,{ responseBody,responseError } from "./api.config";
const requests = {
	get: (url, header) => api.get(url, header).then(responseBody),
	post: (url, body,header) => api.post(url, body, header).then(responseBody),
	put: (url, body) => api.put(url, body).then(responseBody),
	patch: (url, body, header) => api.patch(url, body, header).then(responseBody),
	delete: (url) => api.delete(url).then(responseBody),
};

export const BankApi = {
	getUser:(userId)=> requests.get(`/user/${userId}`),
	createUser:(object)=>requests.post(`/user`,object),
	getAccounts:(userId)=>requests.get(`/accounts/${userId}`),
	updateAccount:(accountId,object)=>requests.put(`/accounts/account/${accountId}`,object),
	login:(object)=>requests.post(`/user/login`,object),
	getAccount:(accountNo)=>requests.get(`/accounts/account/${accountNo}`),
	getControlToken:()=>requests.get(`/user/tokenControl`),
	sendTransfer:(object)=>requests.post(`/transactions/transfer`,object),
	getAllAccount:()=>requests.get(`/accounts`),
	deleteAccount:(id)=>requests.delete(`/accounts/account/${id}`),
	getAllTransactionByToken:()=>requests.get("/transactions"),
	searchAccount:(object)=>requests.post('/accounts/search',object),
	createAccount:(object)=>requests.post('/accounts',object)
}