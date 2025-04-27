from dateutil import parser
import threading
import datetime
import socket
import time

slave_client = socket.socket()

def startSendingTime():
    while True:
        try:
            slave_client.send(str(datetime.datetime.now()).encode())
            print(f"[Client] Send this time to the Server: {datetime.datetime.now()}")

            time.sleep(5)
        except Exception as e:
            print("[Client Sending Error]", e)
            break

def startReceivingTime():
    while True:
        try:
            data = slave_client.recv(1024)
            if not data:
                break
            synchronized_time = parser.parse(data.decode())
            print(f"[Client] Received synchronized time: {synchronized_time}")
        except Exception as e:
            print("[Client Receiving Error]", e)
            break

def initiateSlaveClient(port=8080):
    try:
        slave_client.connect(('localhost', port))
        print("[Client] Connected to server successfully.")

        send_thread = threading.Thread(target=startSendingTime)
        recv_thread = threading.Thread(target=startReceivingTime)

        send_thread.start()
        recv_thread.start()

        send_thread.join()
        recv_thread.join()

    except Exception as e:
        print("[Client Connection Error]", e)

if __name__ == "__main__":
    initiateSlaveClient(8080)
