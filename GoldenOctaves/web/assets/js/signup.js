async function signUp() {

    const popup = Notification();

    const user_dto = {
        first_name: document.getElementById("firstName").value,
        last_name: document.getElementById("lastName").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
    };

    const response = await fetch("SignUp",
            {
                method: "POST",
                body: JSON.stringify(user_dto),
                headers: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {
        const json = await response.json();

        if (json.success) {
            popup.success({
                message: "Account Creation Successful"
            });
            window.location = "verifyaccount.html";
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