import "primereact/resources/themes/lara-light-cyan/theme.css";
import { FloatLabel } from 'primereact/floatlabel';
import { useEffect, useState,useRef } from 'react';
import { TabView, TabPanel } from 'primereact/tabview';
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";
import * as Yup from "yup";
import { emailRegex } from "./constants";
import { BankApi } from "../api/api.services";

export default function CreateUser(){

    const [username,setUsername] = useState('');
    const [password,setPassword] = useState('');
    const [confirmPassword,setConfirmPassword] = useState('');
    const [email,setEmail] = useState('');
    const [errors,setErrors] = useState([]);


    const validateForm = Yup.object().shape({
        username: Yup.string().required('Username is reqired'),
        password: Yup.string().required('Password is required'),
        confirmPassword:Yup.string().required("Confirm password is required")
        .oneOf([Yup.ref('password'), null], 'Passwords is not same'),
       email: Yup.string().email().required('Email is wrong format').matches(emailRegex),

    });

    

    const saveUser = async (e)=>{
        e.preventDefault();
        const user = {
            username:username,
            password:password,
            confirmPassword:confirmPassword,
            email:email
        };
        const err = validateForm.validate(user,{abortEarly :false})
        .then(t =>{
            setErrors([]);
            BankApi.createUser(user).then(response=>{
                              
                alert("Kayıt başarılı");
                setUsername("");
                setPassword("");
                confirmPassword("");
                setEmail("");
            });
            

        } 
        )
        .catch((c)=>{
            let err = [];
            c.errors.map(s=>{
                
                if(s === 'Username is reqired'){
                    err[0] = true;
                }
                else if(s === 'Password is required'){
                    err[1] = true;
                }else if(s === 'Passwords is not same'){
                    err[2] = true;
                }else if(s === 'Confirm password is required')err[2]=true;
                else if(s === 'Email is wrong format')err[3]= true;
                else if(s === 'email must be a valid email')err[3]= true;
                else if(s.startsWith("email must match the following"))err[3]=true;
            })
            setErrors(err);
        });

        
    }

    

    return(    
        <form>
            <div>
                <FloatLabel className='label'>
                    <InputText type='text' id="username"  value={username} onChange={(e)=>setUsername(e.target.value)}  />
                    <label htmlFor="username">Username</label>                    
                </FloatLabel>
                {errors[0] && <label className="error">Username is require</label>}
            </div>

            <div>
                <FloatLabel className='label'>
                    <InputText type='text' id="password" value={password}  onChange={(e)=>setPassword(e.target.value)}/>
                    <label htmlFor="password">Password</label>
                </FloatLabel>
                {errors[1] && <label  className="error">Password is require</label>}
            </div>

    
            <div>
                <FloatLabel className='label'>
                    <InputText type='text'  id="confirmPassword"  value={confirmPassword} onChange={(e)=>setConfirmPassword(e.target.value)} />
                    <label htmlFor="confirmPassword">Confirm Password</label>
                </FloatLabel>
                {errors[2] && <label  className="error">Password is not matching</label>}
            </div>
    
            <div>
                <FloatLabel className='label'>
                    <InputText type='text' id="userEmail" value={email} onChange={(e)=>setEmail(e.target.value)} />
                    <label htmlFor="userEmail">Email</label>
                </FloatLabel>
                {errors[3] && <label  className="error">Email is not valid</label>}
            </div>

    
            <Button label='Create User' onClick={saveUser}/>
    
        </form>);
}