import { main as processWithAI } from "./openAi-integration.js"; // Use relative path

const taskStore = new Map(); // Store tasks in memory

document.addEventListener("DOMContentLoaded", () => {
  const voiceButton = document.querySelector(".voice-btn"); // Fixed class name
  if (voiceButton) {
    voiceButton.addEventListener("click", startListening); // Fixed spelling
  }
  clearTaskOutput();
});

function clearTaskOutput() {
  document.getElementById('operation').textContent = '';
  document.getElementById('task').textContent = '';
  document.getElementById('urgency').textContent = '';
  document.getElementById('datetime').textContent = '';

  const confirmationArea = document.getElementById('confirmation-area');
  if (confirmationArea) {
    confirmationArea.textContent = '';
  }
}

function startListening() { // Fixed spelling
  if ('webkitSpeechRecognition' in window) {
    const recognition = new webkitSpeechRecognition();
    recognition.continuous = false;
    recognition.interimResults = false;
    recognition.lang = "en-US";

    recognition.onstart = function () {
      console.log("Voice recognition started. Speak clearly....");
      clearTaskOutput();
    };

    recognition.onresult = function (event) {
      const transcript = event.results[0][0].transcript;
      console.log("Transcript:", transcript);
      processVoiceCommand(transcript);
    };

    recognition.onerror = function (event) {
      console.log("Error in voice recognition:", event.error);
    };

    recognition.start();
  } else {
    alert("Speech recognition not supported.");
  }
}


async function processVoiceCommand(command) {
  try {
    console.log("Processing command:", command);
    
    const aiResp = await processWithAI(command);
    if (!aiResp || !aiResp.choices || aiResp.choices.length === 0) {
      throw new Error("Invalid AI response: No choices received");
    }

    const aiResponse = JSON.parse(aiResp.choices[0].message.content);
    console.log("AI Response:", aiResponse);

    if (!aiResponse) {
      throw new Error("Invalid AI response format");
    }

    const requestBody = {
      operation: aiResponse.operation,
      task: aiResponse.task,
      urgency: aiResponse.urgency,
      datetime: aiResponse.datetime,
    };

    // Update UI elements
    document.getElementById("operation").textContent = aiResponse.operation ? `Operation: ${aiResponse.operation}` : "Operation: Not specified";
    document.getElementById("task").textContent = aiResponse.task ? `Task: ${aiResponse.task}` : "Task: Not specified";
    document.getElementById("urgency").textContent = aiResponse.urgency ? `Urgency: ${aiResponse.urgency}` : "Urgency: Not specified";
    document.getElementById("datetime").textContent = aiResponse.datetime ? `Date and Time: ${aiResponse.datetime}` : "Date and Time: Not specified";

    // Confirmation message
    const confirmationArea = document.getElementById("confirmation-area");
    if (confirmationArea) {
      confirmationArea.textContent = `Are you sure you want to ${aiResponse.operation} the task "${aiResponse.task}"?`;

      // Confirm button
      const confirmButton = document.createElement("button");
      confirmButton.textContent = "Confirm";
      confirmButton.addEventListener("click", async () => {
        try {
          const result = await sendTaskToServer(requestBody);
          alert(result ? "Task added successfully!" : "Failed to add task.");
        } catch (error) {
          console.error("Error sending task:", error);
          alert("An error occurred while adding the task.");
        }
      });

      confirmationArea.appendChild(confirmButton);
    }

    // Send task to server
    const response = await fetch("http://localhost:8080/api/tasks", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Accept": "application/json",
      },
      body: JSON.stringify(requestBody),
    });

    if (!response.ok) {
      throw new Error(`HTTP error with status code: ${response.status}`);
    }

    const responseData = await response.json();
    console.log("Task added successfully:", responseData);
    return responseData;
  } catch (error) {
    console.error("Error processing voice command:", error);
    return null;
  }
}

