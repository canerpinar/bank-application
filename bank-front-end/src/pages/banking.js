import { FloatLabel } from 'primereact/floatlabel';
import { TabView, TabPanel } from 'primereact/tabview';
import { InputText } from 'primereact/inputtext';
import { useEffect, useState,useRef } from 'react';
import { Button } from 'primereact/button';
import { ListBox } from 'primereact/listbox';
import { BankApi } from '../api/api.services';
import { useAuthState,useAuthDispatch,logout } from '../Context';
import 'primeicons/primeicons.css';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputNumber } from 'primereact/inputnumber';
import { Dropdown } from 'primereact/dropdown';
import { Tag } from 'primereact/tag';
import { responseBody } from '../api/api.config';
import { useAsyncError } from 'react-router-dom';

export default function Banking(){
    const dispatch = useAuthDispatch();
    

    const [accountNo,setAccountNo] = useState('');
    const [amount,setAmount] = useState('');
    const [accounts,setAccounts] = useState([]);
    const [selectedAccount,setSelectedAccount] = useState(null);
    const [toName,setToName] = useState("");
    const [loading,setLoading] = useState(false);
    const [allAccount,setAllAccount] = useState([]);
    const [statuses] = useState(['INSTOCK', 'LOWSTOCK', 'OUTOFSTOCK']);
    const userDetails  = useAuthState();
    const [searchAccount,setSearchAccount] = useState('');
    const [transactionHistory,setTransactionHistory] = useState([]);
    const [searchedAccounts,setSearchedAccounts] = useState([]);

    const getAllAccount = ()=>{
        BankApi.getAllAccount().then(response=>{
            setAllAccount(response);

        }).catch(error=>{
            if(error.status === 403){
                logout(dispatch);
            }
        });


        
    }

    useEffect(()=>{

        getAllAccount();
        getTransactionHistory();
        setAccounts(userDetails.accounts);
    },[]);

    const getTransactionHistory = ()=>{
        BankApi.getAllTransactionByToken().then(response=>{
            setTransactionHistory(response);
        });
    }



    const delay = ms => new Promise(res => setTimeout(res, ms));
    useEffect(()=>{
        const getAccount = async ()=>{
            setLoading(true);
          try {
            const response = await BankApi.getAccount(accountNo);
            
            await delay(2000);
            setLoading(false);
            setToName(response.username);
            if(response.statusCode === 404){
                setToName('');
            }
          } catch (error) {
            if(error.response.status === 404){
                setToName('');
            }
            await delay(2000);
            setLoading(false);
          }
            
        } 
        if(accountNo !== '' && amount !== ''){
            getAccount();
        }

    },[accountNo,amount]);
    const transfer = async ()=>{
        const request = {
            fromAccount:selectedAccount.number,
            toAccount:accountNo,
            amount:amount
        }
        try {
            if(selectedAccount === null){
                alert("You must select account");return;
            } 
            BankApi.sendTransfer(request).then(response=>{
                if(response.statusCode === 200){
                    alert("Your transfer success");
                    getTransactionHistory();
                    getAllAccount();
                    searchAccountByNameAndNumber("");
                }else if(response.statusCode === 403){
                    getTransactionHistory();
                    alert(response.message);
                }
            }).catch(error =>{
                if(error.status === 403){
                    logout(dispatch);
                }
            });
        } catch (error) {
            console.log(error);
        }

    }

    
    const onRowEditComplete = (e) => {
        let _accounts = [...allAccount];
        let { newData, index } = e;

        _accounts[index] = newData;
        BankApi.updateAccount(newData.id,newData).then(response=>{
            
            setAllAccount(_accounts);
            alert("Update success");
        });
        
    };

    const textEditor = (options) => {
        return <InputText type="text" value={options.value} onChange={(e) => options.editorCallback(e.target.value)} />;
    };

    const statusEditor = (options) => {
        return (
            <Dropdown
                value={options.value}
                options={statuses}
                onChange={(e) => options.editorCallback(e.value)}
                placeholder="Select a Status"
                itemTemplate={(option) => {
                    return <Tag value={option} severity={getSeverity(option)}></Tag>;
                }}
            />
        );
    };

    const priceEditor = (options) => {
        return <InputNumber value={options.value} onValueChange={(e) => options.editorCallback(e.value)} mode="currency" currency="USD" locale="en-US" />;
    };

    const statusBodyTemplate = (rowData) => {
        return <Tag value={rowData.username} severity={getSeverity(rowData.username)}></Tag>;
    };

    const priceBodyTemplate = (rowData) => {
        return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(rowData.balance);
    };

    const allowEdit = (rowData) => {
        return rowData.name !== 'Blue Band';
    };

    const getSeverity = (value) => {
        switch (value) {
            case 'INSTOCK':
                return 'success';

            case 'LOWSTOCK':
                return 'warning';

            case 'OUTOFSTOCK':
                return 'danger';

            default:
                return null;
        }
    };

    const deleteAccount = (options)=>{

        if(window.confirm("Are you delete other person account")){
            BankApi.deleteAccount(options.id).then(response=>{
                let _accounts = [...allAccount];
                //let { newData, index } = options;
                var index = _accounts.indexOf(options)
                if (index !== -1) {
                    _accounts.splice(index, 1);
                  setAllAccount(_accounts);
                }
                alert("Delete success");
    
            })
        }

    }
    
    const deleteBody = (options)=>{
        
        return(

            <i className="pi pi-trash"  onClick={()=>deleteAccount(options)} style={{ fontSize: '2rem',cursor:'pointer' }}></i>
        )
    }


    const searchAccountByNameAndNumber = (_value)=>{
        const request = {value:_value}
        setSearchAccount(_value);
        BankApi.searchAccount(request).then(response=>{
            setSearchedAccounts(response.body);
        });
    }


    const saveAccount = async (e)=>{
        e.preventDefault();
        const request = {number:newAccountNo,name:newAccountName,balance:newBalance}
        const response = await BankApi.createAccount(request);
        if(response.statusCode === 200){
            const _accounts = [...accounts];//we return array and add new return object
            _accounts.push(response.body);
            setAccounts(_accounts);
            alert("Saved success");

        }else console.log(response);

    }

    const [newAccountName,setNewAccountName] = useState('');
    const [newAccountNo,setNewAccountNo] = useState(''); 
    const [newBalance,setNewBalance] = useState('');

    return(
        <TabView activeIndex={1}>

            <TabPanel header="Accounts">
                    <DataTable value={allAccount} editMode="row" tableStyle={{ minWidth: '50rem' }} onRowEditComplete={onRowEditComplete} >
                        <Column field="id" header="ID" editor={(options) => textEditor(options)} style={{ width: '20%' }}></Column>
                        <Column field="name" header="Name" editor={(options) => textEditor(options)} style={{ width: '20%' }}></Column>
                        <Column field="username" header="Who" body={statusBodyTemplate} editor={(options) => statusEditor(options)} style={{ width: '20%' }}></Column>
                        <Column field="balance" header="Current Balance" body={priceBodyTemplate} editor={(options) => priceEditor(options)} style={{ width: '20%' }}></Column>
                        <Column rowEditor={allowEdit} headerStyle={{ width: '10%', minWidth: '8rem' }} bodyStyle={{ textAlign: 'center' }}></Column>
                        <Column header="Delete" body={(options)=>deleteBody(options)}>
                            
                        </Column>
                    </DataTable> 
            </TabPanel>

      
            <TabPanel header="Search Account">
                <InputText type="text" value={searchAccount} onChange={(e) => searchAccountByNameAndNumber(e.target.value)} />
                <DataTable value={searchedAccounts}>
                        <Column field="name" header="NAME"  style={{ width: '20%' }}></Column>
                        <Column field="username" header="WHO" style={{ width: '20%' }}></Column>
                        <Column field="balance" header="BALANCE"  style={{ width: '20%' }}></Column>
                        <Column field="number" header="IBAN"  style={{ width: '20%' }}></Column>
                    </DataTable>
            </TabPanel>


            <TabPanel header="Create Account">                        
                    <form>
                        <div>
                            <FloatLabel className='label'>
                                <InputText type='text' id='accounName' value={newAccountName} onChange={(e)=>setNewAccountName(e.target.value)}/>
                                <label htmlFor='accounName'>Account Name</label>
                            </FloatLabel>

                        </div>

                        <div>
                            <FloatLabel className='label'>
                                <InputText type='text' id='accountNo' value={newAccountNo} onChange={(e)=>setNewAccountNo(e.target.value)}/>
                                <label htmlFor='accountNo'>Account No</label>
                            </FloatLabel>

                        </div>

                
                        <div>
                            <FloatLabel className='label'>
                                <InputText type='text'  id='balance' value={newBalance} onChange={(e)=>setNewBalance(e.target.value)}/>
                                <label htmlFor='babalce'>Balance</label>
                            </FloatLabel>

                        </div>
                


                
                        <Button label='Create Account' onClick={saveAccount}/>
                
                    </form>

            </TabPanel>

            <TabPanel header="Transaction History">

                    <DataTable value={transactionHistory}>
                        <Column field="transactionDate" header="DATE"  style={{ width: '20%' }}></Column>
                        <Column field="amount" header="AMOUNT" style={{ width: '20%' }}></Column>
                        <Column field="transactionStatus" header="STATUS"  style={{ width: '20%' }}></Column>
                    </DataTable>
            </TabPanel>

            <TabPanel header="Transfer - EFT">
                <FloatLabel>
                    <ListBox filter value={selectedAccount} id='accounts' placeholder='Choose Your Account ' onChange={(e) => setSelectedAccount(e.value)} options={accounts} optionLabel="name" style={{width:250}} />  
                </FloatLabel><label htmlFor="accounts">Choose Your Account</label>

                <div>
                    <FloatLabel className='label'>            
                        <InputText type='text' id="accountNumber"  value={accountNo} onChange={(e)=>setAccountNo(e.target.value)} />
                        <label htmlFor="accountNo">Account No</label>
                        {loading === true ? <i className="pi pi-spin pi-spinner small" style={{ fontSize: '2rem'}}></i> : toName !=='' ? <i className="pi pi-check small"></i> : <i className="pi pi-times"></i>}
                    </FloatLabel>
                </div>

                <FloatLabel className='label'>            
                    <InputText type='text' id="Amount"  value={amount} onChange={(e)=>setAmount(e.target.value)} />
                    <label htmlFor="amount">Amount</label>
                </FloatLabel>

                <FloatLabel className='label'>            
                    <InputText style={{color:'green'}} type='text' id="personUsername"  value={toName} />
                    <label htmlFor="personUsername">Receipt Name</label>
                </FloatLabel>
        
                <Button label='Transfer' onClick={transfer}/>
            </TabPanel>
         
    </TabView>

    )
}