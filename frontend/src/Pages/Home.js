import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { getAccessToken, removeTokens } from "../Utils/Authentication"

function GetAccounts() {
    const [user, setUser] = useState("");
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
            }
            else {
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
        }
        else {
            console.log(response.data);
            alert("Error Logging Out! Try Again!");
        }
    };

    return (
        <div>
            <h1>Welcome {user.firstName}!</h1>
            <button onClick={handleLogout}>Logout</button>
        </div>
    );
};

export default GetAccounts;