from http.server import HTTPServer, SimpleHTTPRequestHandler
import os
import sys
import time
from threading import Thread

class CoverageReportHandler(SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        # Ensure the directory exists
        os.makedirs("src/test/resources", exist_ok=True)
        super().__init__(*args, directory="src/test/resources", **kwargs)

    def end_headers(self):
        # Add CORS headers
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Content-Type', 'text/html')
        super().end_headers()

    def do_GET(self):
        try:
            if self.path == '/':
                self.path = '/coverage-report.html'
            elif self.path.startswith('/coverage/'):
                # Serve JaCoCo reports from the coverage directory
                pass
            
            # Try to serve the file
            try:
                super().do_GET()
            except Exception as e:
                print(f"Error serving file {self.path}: {e}")
                self.send_error(404, "File not found")
                
        except Exception as e:
            print(f"Error handling request: {e}")
            self.send_error(500, f"Internal server error: {str(e)}")

def start_server():
    server_address = ('0.0.0.0', 8080)
    try:
        httpd = HTTPServer(server_address, CoverageReportHandler)
        print(f"Starting coverage report server on port 8080...")
        print(f"Coverage report should be available at: http://0.0.0.0:8080")
        sys.stdout.flush()
        httpd.serve_forever()
    except Exception as e:
        print(f"Error starting server: {e}")
        sys.stdout.flush()
        sys.exit(1)

if __name__ == '__main__':
    # Start the server in a separate thread
    server_thread = Thread(target=start_server)
    server_thread.daemon = True
    server_thread.start()
    
    # Keep the main thread alive and give time for the server to start
    time.sleep(2)
    
    # Keep the script running
    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        print("Server stopping...")
        sys.exit(0)
