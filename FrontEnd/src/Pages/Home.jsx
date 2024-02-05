import React, { useState, useEffect } from "react";
import "../styling/Home.css"; // Import the CSS file

const HomePage = () => {
  const [title, setTitle] = useState("");
  const [price, setPrice] = useState("");
  const [quantity, setQuantity] = useState("");
  const [file, setFile] = useState(null);
  const [galleryName, setGalleryName] = useState(null);
  const [description, setDescription] = useState(null);
  const [notification, setNotification] = useState({ message: "", type: "" });

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append("file", file);
    formData.append("title", title);
    formData.append("price", price);
    formData.append("quantity", quantity);
    formData.append("galleryName", galleryName);
    formData.append("description", description);

    try {
      const response = await fetch(
        "http://localhost:8082/products-service/products/create",
        {
          method: "POST",
          body: formData,
        }
      );

      if (response.ok) {
        // Handle success, e.g., redirect to a success page or show a success message
        setNotification({
          message: "Product created successfully!",
          type: "success",
        });
      } else {
        // Handle error, e.g., show an error message
        setNotification({
          message: `Error creating product: ${response.statusText}`,
          type: "error",
        });
      }
    } catch (error) {
      setNotification({
        message: `Error creating product: ${error.message}`,
        type: "error",
      });
    }
  };

  useEffect(() => {
    // Function to hide the notification after a certain duration (e.g., 3000 milliseconds or 3 seconds)
    const hideNotification = () => {
      setNotification({ message: "", type: "" });
    };

    // Check if there's a notification
    if (notification.message) {
      // Set a timeout to hide the notification after 3000 milliseconds (3 seconds)
      const timeoutId = setTimeout(hideNotification, 3000);

      // Clean up the timeout when the component unmounts or when a new notification is shown
      return () => clearTimeout(timeoutId);
    }
  }, [notification]);

  return (
    <div className="page-container">
      <div id="centered-container">
        <h1 className="home-heading">Product Creation</h1>

        <form className="product-form">
          <div className="form-group">
            <label htmlFor="title">Title:</label>
            <input
              type="text"
              id="title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="Enter product title"
            />
          </div>

          <div className="form-group">
            <label htmlFor="price">Price:</label>
            <input
              type="number"
              id="price"
              value={price}
              onChange={(e) => setPrice(e.target.value)}
              placeholder="Enter product price"
            />
          </div>

          <div className="form-group">
            <label htmlFor="quantity">Quantity:</label>
            <input
              type="number"
              id="quantity"
              value={quantity}
              onChange={(e) => setQuantity(e.target.value)}
              placeholder="Enter product quantity"
            />
          </div>

          <div className="form-group">
            <label htmlFor="galleryName">Gallery Name:</label>
            <input
              type="text"
              id="galleryName"
              value={galleryName}
              onChange={(e) => setGalleryName(e.target.value)}
              placeholder="Enter product gallery name"
            />
          </div>

          <div className="form-group">
            <label htmlFor="file">File:</label>
            <input
              type="file"
              id="file"
              onChange={(e) => setFile(e.target.files[0])}
            />
          </div>

          <div className="form-group">
            <label htmlFor="description">Description:</label>
            <textarea
              id="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="Enter product description"
              rows={6} // Set the number of visible rows
            />
          </div>

          <button type="button" onClick={handleSubmit}>
            Create Product
          </button>
        </form>
      </div>

      {/* Notification Box */}
      {notification.message && (
        <div className={`notification-box ${notification.type}`}>
          {notification.message}
        </div>
      )}
    </div>
  );
};

export default HomePage;
