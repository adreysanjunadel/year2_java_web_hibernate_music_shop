async function loadIndexFeaturedProducts() {
    const response = await fetch("LoadFeaturedProducts");

    const popup = Notification();

    if (response.ok) {
        const json = await response.json();
        const productList = json.products;

        productList.forEach((product, index) => {
            const i = index + 1;
            const titleElement = document.getElementById("st-product-title-" + i);
            const priceElement = document.getElementById("st-product-price-" + i);
            const linkElement = document.getElementById("st-product-link-" + i);
            const imageElement = document.getElementById("st-product-image-" + i);

            if (titleElement) titleElement.innerHTML = product.title;
            if (priceElement) priceElement.innerHTML = new Intl.NumberFormat("en-US", { minimumFractionDigits: 2 }).format(product.price);
            if (linkElement) linkElement.href = "singleproduct.html?id=" + product.id;
            if (imageElement) {
                imageElement.src = "product-images/" + product.id + "/image1.png";
                imageElement.onerror = () => imageElement.src = "path/to/default/image.png"; // Fallback image if the source is invalid
            }
        });

    } else {
        popup.error({ message: "Unable to process your request!" });
    }
}

async function loadSimilarProducts() {
    try {
        // Fetch similar products data
        const response = await fetch("LoadSimilarProducts"); // Adjust URL as needed
        if (!response.ok) throw new Error("Network response was not ok.");

        // Parse the JSON data
        const json = await response.json();
        const productList = json.products; // Adjust property name as needed

        // Create the HTML for the similar products section
        let similarProductsHtml = `
            <div class="mt-5 mb-5">
                <h2 class="text-light text-center"><i class="bi bi-bookmark-check"></i> Similar Products</h2>
                <div class="row row-cols-4 g-3 mt-3"> <!-- Set 4 columns per row -->
        `;

        // Define fixed dimensions for images
        const imageWidth = "200px";
        const imageHeight = "200px";

        // Generate product cards
        productList.forEach(item => {
            const imgSrc = "product-images/" + item.id + "/image1.png";

            similarProductsHtml += `
                <div class="col">
                    <div class="product-card p-3" style="height: 24rem;">
                        <img src="${imgSrc}" class="fixed-image-size rounded" alt="${item.title}" style="width: ${imageWidth}; height: ${imageHeight}; object-fit: cover;">
                        <h5 class="mt-5 text-center">${item.title}</h5>
                        <p class="text-warning text-center">Rs. ${new Intl.NumberFormat("en-US", { minimumFractionDigits: 2 }).format(item.price)}</p>
                        <div class="hover-icons mt-5">
                            <button class="panel-btn"><i class="bi bi-eye"></i></button>
                            <button class="panel-btn" style="width: 40%; border-radius: 5%;" onclick="window.location.href='singleproduct.html?id=${item.id}';"><i class="bi bi-tag"></i>&nbsp;&nbsp;Shop Now</button>
                            <button class="panel-btn"><i class="bi bi-cart"></i></button>
                        </div>
                    </div>
                </div>
            `;
        });

        similarProductsHtml += `</div></div>`;

        // Insert the generated HTML into the document
        const container = document.querySelector(".similar-products-container"); // Adjust selector as needed
        if (container) {
            container.innerHTML = similarProductsHtml;
        } else {
            console.error("Container for similar products not found.");
        }
    } catch (error) {
        console.error("Error loading similar products:", error);
    }
}

async function viewCart(){
    
    const response = await  fetch("cart.html");
    
    if (response.ok){
        
        const cartHtmlText = await  response.text();
        
        const parser = new DOMParser();
        const cartHtml = parser.parseFromString(cartHtmlText,"text/html");       
        
        const cart_Main = cartHtml.getElementById("st-cart-main");
        
        document.getElementById("st-index-main").innerHTML = cart_Main.innerHTML;
        
        loadCartItems();
        
    }
    
}