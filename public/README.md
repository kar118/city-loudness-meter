## Running without proper backend
For development purposes, this frontend can be served as static content by [nginx](https://www.nginx.com/) web server with the following config snippet. Mock API responses from `./api` directory will be used.
```
server {
    listen 80;
    server_name local;

    root /path/to/public;

    location / {
        index index.htm;
        try_files $uri $uri.htm $uri/ index.htm;
    }
}
```