FROM busybox:latest
RUN pwd; ls -la
COPY app.jar /tmp/
CMD cp /tmp/app.jar /app && tail -f /dev/null
