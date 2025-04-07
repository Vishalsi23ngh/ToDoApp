import OpenAI from "https://cdn.skypack.dev/openai";
import { GITHUB_TOKEN } from "./config.js"; // Import token from config.js

export async function main(command) {
  if (!GITHUB_TOKEN) {
    console.error("Missing GITHUB_TOKEN. Set it in config.js");
    return;
  }

  const client = new OpenAI({
    baseURL: "https://models.inference.ai.azure.com",
    apiKey: GITHUB_TOKEN,
    dangerouslyAllowBrowser: true // Allow running in the browser
  });

  try {
    const response = await client.chat.completions.create({
      messages: [
        {
          role: "system",
          content: `You are a task analyzer. Extract the following details from the provided command:
                    1. Operation (Add/Delete/Update)
                    2. Task description
                    3. Urgency (High/Medium/Low)
                    4. Date and Time (if mentioned)

                    Respond in JSON format like:
                    {
                      "operation": "...",
                      "task": "...",
                      "urgency": "...",
                      "datetime": "..." (or null if not specified)
                    }

                    Keep the task field case-insensitive for comparison purposes.`
        },
        {
          role: "user",
          content: command
        }
      ],
      model: "gpt-4o",
      temperature: 1,
      max_tokens: 4096,
      top_p: 1
    });

    // Ensure response is valid
    if (!response.choices || response.choices.length === 0) {
      console.error("Invalid response from OpenAI API");
      return null;
    }

    console.log(response.choices[0].message.content);
    return response.choices[0].message.content;

} catch (error) {
    console.error("Error calling OpenAI API:", error);
    return null;
}
}
