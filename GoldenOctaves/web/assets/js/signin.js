async function signIn() {

    const popup = Notification();

    const user_credentials_dto = {
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    };

    const response = await fetch("SignIn",
            {
                method: "POST",
                body: JSON.stringify(user_credentials_dto),
                headers: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {
        const json = await response.json();

        if (json.success) {
            window.location = "index.html";

        } else {

            if (json.content === "Unverified") {
                window.location = "verifyaccount.html";
            } else {
                popup.error({
                    message: json.content
                });
            }

        }

    } else {
        popup.error({
            message: "Please try again later!"
        });
    }

}