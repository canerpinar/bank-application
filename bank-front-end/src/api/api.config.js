import { error } from "ajv/dist/vocabularies/applicator/dependencies";
import axios, { AxiosResponse } from "axios";
import { logout } from "../Context";
import { useAuthDispatch } from "../Context";
export default axios.create({
	baseURL: "http://localhost:8080/api",
	withCredentials: true,
	timeout: 40000
});



export const responseBody = (response) => response.data;

axios.interceptors.response.use(
	
	response => response,
	error => {
	  console.error('API call failed:', error);
	  // Handle specific error cases
	  if (error.response.status === 401) {
		// Unauthorized
	  } else if (error.response.status === 404) {
		// Not found
	  }else if (error.response.status === 403) {

	  }
	  return Promise.reject(error);
	}
  )
