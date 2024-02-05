// Products.js
import React, { useEffect, useState, useRef } from 'react';
import '../styling/Products.css';

const Products = ({ cartItems, updateCart }) => {
  const [galleries, setGalleries] = useState([]);
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [showMessage, setShowMessage] = useState(false);
  const messageRef = useRef(null);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await fetch('http://localhost:8084/products');
        if (response.ok) {
          const data = await response.json();

          const galleriesMap = new Map();

          data.forEach((product) => {
            const { galleryName } = product;
            if (!galleriesMap.has(galleryName)) {
              galleriesMap.set(galleryName, { title: galleryName, products: [] });
            }
            galleriesMap.get(galleryName).products.push({
              ...product,
              imageUrl: `http://localhost:8084/products/images/${product.imageId}`,
            });
          });

          const galleriesArray = Array.from(galleriesMap.values());
          setGalleries(galleriesArray);
        } else {
          console.error('Error fetching product details:', response.statusText);
        }
      } catch (error) {
        console.error('Error fetching product details:', error.message);
      }
    };

    fetchProducts();
  }, []);

  const openModal = (product) => {
    // Check if the clicked element is not the "Add to Cart" button
    if (!document.activeElement.classList.contains('add-to-cart')) {
      setSelectedProduct(product);
      document.getElementById('myModal').style.display = 'block';
    }
  };

  const closeModal = () => {
    setSelectedProduct(null);
    document.getElementById('myModal').style.display = 'none';
  };

  const addToCart = (product) => {
    const updatedCart = [...cartItems, { ...product, quantity: 1 }];
    updateCart(updatedCart);
    setShowMessage(true);

   // Show the message
   messageRef.current.style.display = 'block';

   // Set a timer to hide the message after 3 seconds (adjust as needed)
   setTimeout(() => {
     messageRef.current.style.display = 'none';
   }, 3000);
 };

 const getDate = ((date)=>{

  console.log(date);
  const dateObject = new Date(date);
  

  // Format the date according to the user's locale
  const formattedDate = dateObject.toLocaleString();

  return (
    formattedDate
  );
 });

 return (
   <div>
     <div className="page-heading">
       <h1>Products</h1>
     </div>
     <div className="products-container">
        {galleries.map((gallery, index) => (
          <div key={index} className="gallery">
            <h3>{gallery.title}</h3>
            <div className="product-gallery">
              {gallery.products.map((product) => (
                <div key={product.id} className="product-entity">
                  <div
                    className={`product-box ${product.quantity === 0 ? 'zero-quantity' : ''}`}
                    onClick={() => openModal(product)}
                  >
                    <div className="product-img">
                      <img src={product.imageUrl} alt={product.title} />
                    </div>
                  </div>
                  <div
                    className={`product-name ${product.quantity === 0 ? 'zero-quantity' : ''}`}
                    onClick={() => openModal(product)}
                  >
                    <p>{product.title}</p>
                    <button className="add-to-cart" onClick={() => addToCart(product)}>
                      +{/* You can use an icon here if you have an icon library */}
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        ))}
      </div>

     <div id="myModal" className="modal" onClick={closeModal}>
       <div className="modal-content" onClick={(e) => e.stopPropagation()}>
         <span className="close" onClick={closeModal}>&times;</span>
         {selectedProduct && (
           <div>
             <h2>{selectedProduct.title}</h2>
             <div className="product-details">
               <div className="product-img">
                 <img src={selectedProduct.imageUrl} alt={selectedProduct.title} />
               </div>
               <div className="details">
                <p data-label="Quantity: ">{selectedProduct.quantity}</p>
                <p data-label="Description: ">{selectedProduct.description}</p>
                <p data-label="CreatedOn: ">{getDate(selectedProduct.createdOn)}</p>
                 <button onClick={() => addToCart(selectedProduct)}>
                   Add to Cart
                 </button>
               </div>
             </div>
           </div>
         )}
       </div>
     </div>

     {/* Message div with ref */}
     <div ref={messageRef} className="cart-message">Item added to the cart!</div>
   </div>
 );
};

export default Products;