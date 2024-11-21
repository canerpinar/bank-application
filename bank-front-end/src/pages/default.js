import "primereact/resources/themes/lara-light-cyan/theme.css";
import { FloatLabel } from 'primereact/floatlabel';
import { useEffect, useState,useRef } from 'react';
import { TabView, TabPanel } from 'primereact/tabview';
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";
import * as Yup from "yup";
import { emailRegex } from "../utils/constants";
import { BankApi } from "../api/api.services";
import CreateUser from "../utils/createUser";
import { loginUser, useAuthState, useAuthDispatch } from '../Context';
export default function Default(){
    const [password,setPassword] = useState('');

    const [email,setEmail] = useState('');
    const [errors,setErrors] = useState([]);  
    const [loginStatus,setLoginStatus] = useState(false);
  
    const dispatch = useAuthDispatch();
  
    useEffect(()=>{
      if(emailRegex.test(email) && password !== ""){
        setLoginStatus(true);
      }
    },[]);
  
  
  
    const controlValidation =(e)=>{
      let err = [...errors];
      if(e.target.id === "email"){
        setEmail(e.target.value);
        if(emailRegex.test(email) && password !== ""){
          setLoginStatus(true);
          err[0] = false;   
        }else {        
          err[0] = true;
          setLoginStatus(false);
        }
        setErrors(err);
      }
      else if(e.target.id === "password") {
        setPassword(e.target.value);
        let err = [...errors];
        if(emailRegex.test(email) && password !== ""){
          setLoginStatus(true);                    
          err[1] = false;
        }else{
          err[1] = true;
          setLoginStatus(false);
        } 
        setErrors(err);
      }
  
  
  
    }
  
    const login =   (e)=> {
      let err = [...errors];
      e.preventDefault();
      if(email !== "" && password !== ""){
       try {
          let response = loginUser(dispatch, { email:email, password:password }).then(response=>{
            setLoginStatus(true);       
            err[0] = false;
            err[1] = false;
          }).catch(error=>{
            console.log(error);
          });

       } catch (error) {
          console.log(error);
       }

      }else {
        alert("Email doğru değil")
        //setLoginStatus(false);
      }
  
      setErrors(err);
  
  
      
    }
    

    return(    
        <TabView>
        <TabPanel header="Login">
        <form>
          <FloatLabel className='label'>
          
              <InputText type='text' id="email" onBlur={controlValidation} value={email} onChange={controlValidation} />
              {errors[0] === true  ? <label>Control the email</label> : ""}
          </FloatLabel>
          

          <FloatLabel className='label'>
              <InputText type='password' id="password" onBlur={controlValidation} value={password} onChange={controlValidation} /> 
              {errors[1] === true  ? <label>Control the password</label> : ""}     
          </FloatLabel>
        
          
          {loginStatus ? <Button label='Login' onClick={login}/> : <Button  label='Login' onClick={login}/>}
          
          </form>
        </TabPanel>
      
        <TabPanel header="Create User">
          <CreateUser />
        </TabPanel>
    </TabView>
    );
}