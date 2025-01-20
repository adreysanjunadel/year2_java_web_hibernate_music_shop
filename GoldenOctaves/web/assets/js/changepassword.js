async function changePassword() {
    
    const popup = Notification();

    const dto = {
        verification: document.getElementById("verification").value,
        password: document.getElementById("password_a").value,
        confirm_password: document.getElementById("password_b").value,
    };

    const response = await  fetch("ChangePassword",
            {
                method: "POST",
                body: JSON.stringify(dto),
                headers: {
                    "Content-Type": "application/json"
                }
            }
    );


    if (response.ok) {
        const json = await response.json();

        if (json.success) {
            popup.error({
                message: "Password Changed Successfully"
            });
            window.location = "signin.html";

        } else {
            popup.error({
                message: json.content
            });

        }

    } else {
        popup.error({
            message: "Please try again later!"
        });
    }
}


