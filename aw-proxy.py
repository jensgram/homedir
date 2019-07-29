#!/usr/bin/python

import http.server
import socketserver
import sys
import argparse
import urllib.request
import base64

def buildRequestHandler(host, user, pwd):
  class RequestHandler(http.server.SimpleHTTPRequestHandler):
    def do_GET(self):
      if self.path == '/perf.json':
        result = self.getPerfData()

        self.send_response(200)
        self.send_header('Content-type', 'application/json')
        self.send_header('Content-length', len(result))
        self.end_headers()
        self.wfile.write(result)
      else:
        super().do_GET()

    def getPerfData(self):
      url = 'http://' + host + '/json/system/performanceTimers'
      credentials = ('%s:%s' % (user, pwd))
      request = urllib.request.Request(url)
      request.add_header('Authorization', 'Basic ' + base64.b64encode(credentials.encode('ascii')).decode('ascii'))
      fh = urllib.request.urlopen(request)

      return fh.read()

  return RequestHandler

def parseArgs():
  parser = argparse.ArgumentParser(description = 'Proxies AW perf. data from host into `perf.json` as expected by visualizer')

  parser.add_argument('port', type = int, help = 'Port to listen on')
  parser.add_argument('host', help = 'AW host (w/o protocol, incl. port,) e.g. `localhost:9090`')
  parser.add_argument('user', help = 'AW user')
  parser.add_argument('pwd', help = 'AW password')

  return parser.parse_args()

def startServer(port, handler):
  with socketserver.TCPServer(('', port), handler) as httpd:
    print('Serving at port', port)
    httpd.serve_forever()

def main():
  config = parseArgs()

  startServer(config.port, buildRequestHandler(config.host, config.user, config.pwd))

main()