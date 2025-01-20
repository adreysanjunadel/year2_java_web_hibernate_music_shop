async function loadProduct() {
    const parameters = new URLSearchParams(window.location.search);

    if (parameters.has("id")) {
        const productId = parameters.get("id");

        try {
            const response = await fetch("LoadSingleProduct?id=" + productId);

            if (response.ok) {
                const json = await response.json();
                const id = json.product.id;

                // Update the carousel images
                document.getElementById("carousel-image1").src = "product-images/" + id + "/image1.png";
                document.getElementById("carousel-image2").src = "product-images/" + id + "/image2.png";
                document.getElementById("carousel-image3").src = "product-images/" + id + "/image3.png";

                // Update product details
                document.getElementById("product-title").innerHTML = json.product.title;
                document.getElementById("product-price").innerHTML = new Intl.NumberFormat("en-US", {
                    minimumFractionDigits: 2
                }).format(json.product.price);
                
                document.getElementById("product-id").value = json.product.id;
                document.getElementById("product-category").innerHTML = json.product.category.name;
                document.getElementById("product-brand").innerHTML = json.product.model.brand.name;
                document.getElementById("product-model").innerHTML = json.product.model.name;
                document.getElementById("product-origin").innerHTML = json.product.description.origin_Country.name;
                document.getElementById("product-color").innerHTML = json.product.colour.name;
                document.getElementById("product-condition").innerHTML = json.product.product_Condition.name;
                document.getElementById("short-description").innerHTML = json.product.description.short_description;

                // Set up Add to Cart button
                setupAddToCartButton(json.product.id);

                // Similar Products
                let productHtml = document.getElementById("similar-product-1"); // Use this as template
                let productContainer = document.querySelector(".row.row-cols-4"); // Container for similar products

                productContainer.innerHTML = ""; // Clear existing products

                json.productList.forEach(item => {
                    let productCloneHtml = productHtml.cloneNode(true);

                    const imgSrc = "product-images/" + item.id + "/image1.png";

                    // Get the image element and set src
                    let imgElement = productCloneHtml.querySelector("img");
                    imgElement.src = imgSrc;

                    // Handle image loading errors
                    imgElement.onerror = function () {
                        imgElement.src = "path/to/default/image.png"; // Fallback image if the source is invalid
                    };

                    // Update the product details
                    productCloneHtml.querySelector("h5").innerHTML = item.title;
                    productCloneHtml.querySelector("p.text-warning").innerHTML = "Rs.  " + new Intl.NumberFormat("en-US", {
                        minimumFractionDigits: 2
                    }).format(item.price);

                    // Update the Shop Now button with window.location redirect
                    productCloneHtml.querySelector(".bi-tag").parentElement.addEventListener("click", () => {
                        window.location.href = "singleproduct.html?id=" + item.id;
                    });

                    // Add product to container
                    productContainer.appendChild(productCloneHtml);
                });

            } else {
                window.location = "index.html";
            }
        } catch (error) {
            console.error("Failed to load product data:", error);
            window.location = "index.html";
        }
    } else {
        window.location = "index.html";
    }
}

function setupAddToCartButton(productId) {
    const addToCartButton = document.getElementById("add-to-cart-button");

    // Ensure no duplicate event listeners
    addToCartButton.replaceWith(addToCartButton.cloneNode(true));

    // Attach event listener to the new button element
    const newButton = document.getElementById("add-to-cart-button");
    newButton.addEventListener("click", function (e) {
        e.preventDefault();
        handleAddToCart(productId);
    });
}


function handleAddToCart(productId) {
    const quantity = document.getElementById('quantity-input').value;

    // Validate quantity
    if (quantity <= 0) {
        alert("Quantity must be greater than 0");
        return;
    }

    // Disable the button to prevent multiple clicks
    const addToCartButton = document.getElementById("add-to-cart-button");
    addToCartButton.disabled = true;

    // Call the addToCart function
    addToCart(productId, quantity).finally(() => {
        // Re-enable the button once the request is completed
        addToCartButton.disabled = false;
    });
}

async function addToCart(id, qty) {
    try {
        const response = await fetch("AddToCart?id=" + id + "&qty=" + qty);

        const popup = Notification(); // Assuming you have a Notification system in place

        if (response.ok) {
            const json = await response.json();

            if (json.success) {
                popup.success({
                    message: json.content
                });
            } else {
                popup.error({
                    message: json.content
                });
            }
        } else {
            popup.error({
                message: "Unable to process your request!"
            });
        }
    } catch (error) {
        console.error("Failed to add to cart:", error);
        const popup = Notification();
        popup.error({
            message: "An unexpected error occurred."
        });
    }
}
