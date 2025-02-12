import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getAccessToken } from "../Utils/Authentication";
import 'bootstrap/dist/css/bootstrap.css';
import './Login.css';
import CreateAccountForm from "../Components/CreateAccountForm";

function CreateAccount() {
    const [user, setUser] = useState("");
    const [accountRequest, setAccountRequest] = useState({
        userId: "",
        accountType: ""
    });
    const navigate = useNavigate("");

    useEffect(() => {
        const fetchUsers = async () => {
            const response = await fetch("http://localhost:8080/api/users/getLoggedInUser", {
                headers: {
                    Authorization: `Bearer ${getAccessToken()}`
                }
            });
            
            if (response.ok) {
                const data = await response.json();
                setUser(data);
                setAccountRequest({
                    ...accountRequest,
                    userId: data.id
                });
                console.log(user);
            }
            else {
                console.log(response.data);
            }
        };
    
        fetchUsers();
    }, [navigate]);
    
    const handleCreateAccount = async (e) => {
        e.preventDefault();
        const response = await fetch("http://localhost:8080/api/accounts/createAccount", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${getAccessToken()}`
            },
            body: JSON.stringify(accountRequest)
        });

        if (response.ok) {
            const data = await response.json();
            console.log(data);
            navigate("/home");
        }
        else {
            alert("Account Creation Failed. Please try again!");
        }
    };

    const handleChange = (e) => {
        setAccountRequest({
            ...accountRequest,
            [e.target.name]: e.target.value
        });
    };

    return (
        <CreateAccountForm
            handleCreateAccount={handleCreateAccount}
            handleChange={handleChange}
            accountRequest={accountRequest}
        />
    );
};

export default CreateAccount;