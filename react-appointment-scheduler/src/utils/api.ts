console.log(process.env);

// export const getAPIServer = process.env["NODE_ENV"] === "production" ? (schema: "http" | "https" = "http") => `${schema}://localhost:8081/api/` : (schema: "http" | "https" = "http") => `${schema}://localhost:${schema === "http" ? 8087 : 8088}/api/`;
// export const apiServer = getAPIServer("http");

export const apiServer = "http://localhost:10001/api/"