import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getAccessToken, removeTokens } from "../Utils/Authentication";
import 'bootstrap/dist/css/bootstrap.css';
import './Home.css'; // Import the custom CSS

function GetAccounts() {
    const [user, setUser] = useState({});
    const navigate = useNavigate();

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
            } else {
                console.log(response.data);
            }
        };

        fetchUsers();
    }, [navigate]);

    const handleLogout = async () => {
        const response = await fetch("http://localhost:8080/api/users/logout", {
            headers: {
                Authorization: `Bearer ${getAccessToken()}`
            }
        });

        if (response.ok) {
            console.log(response.data);
            removeTokens();
            navigate("/login");
        } else {
            console.log(response.data);
            alert("Error Logging Out! Try Again!");
        }
    };

    return (
        <div className="background-radial-gradient">
            <div className="card bg-glass home-card">
                <div className="card-body">
                    <h1 className="welcome-message">Welcome {user.firstName}!</h1>
                    <div className="account-list">
                        {user.accountList && user.accountList.length > 0 ? (
                            user.accountList.map((account, index) => (
                                <div className="account-item" key={index}>
                                    <p>Account: {account.accountType}</p>
                                    <p>Account Number: {account.accountNumber}</p>
                                    <p>Balance: {account.balance}</p>
                                    <a href={`/account/${account.accountNumber}`} className="btn btn-primary">View Account</a>
                                </div>
                            ))
                        ) : (
                            <p>No accounts available.</p>
                        )}
                    </div>
                    <button className="btn btn-primary logout-button" onClick={handleLogout}>Logout</button>
                </div>
            </div>
        </div>
    );
}

export default GetAccounts;