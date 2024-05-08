if (!process.env.API_URL) {
    console.error('API_URL environmental variable is missing!');
    process.exit();
    }

export const API_URL = process.env.API_URL;