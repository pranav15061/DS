from dateutil import parser
import threading
import datetime
import socket
import time

client_data = {}

def startReceivingClockTime(connector, address):
    while True:
        try:
            clock_time_string = connector.recv(1024).decode()
            if not clock_time_string:
                break
            clock_time = parser.parse(clock_time_string)
            clock_time_difference = datetime.datetime.now() - clock_time

            client_data[address] = {
                "clock_time": clock_time,
                "time_difference": clock_time_difference,
                "connector": connector
            }

            print("Client Data updated with: " + str(address))
            time.sleep(5)

        except Exception as e:
            print(f"Connection with {address} closed.")
            break

def startConnecting(master_server):
    while True:
        try:
            master_slave_connector, addr = master_server.accept()
            slave_address = str(addr[0]) + ":" + str(addr[1])
            print(slave_address + " got connected successfully")

            current_thread = threading.Thread(
                target=startReceivingClockTime,
                args=(master_slave_connector, slave_address,)
            )
            current_thread.start()

        except socket.timeout:
            continue  # allow checking if server is shutting down
        except Exception as e:
            print("Error in accepting connection:", e)
            break

def calculateAvgDifference():
    current_client_data = client_data.copy()
    if len(current_client_data) == 0:
        return datetime.timedelta(0)

    time_difference_list = [
        client["time_difference"]
        for client_addr, client in current_client_data.items()
    ]

    sum_of_clock_difference = sum(
        time_difference_list,
        datetime.timedelta(0, 0)
    )

    average_clock_difference = (
        sum_of_clock_difference / len(current_client_data)
    )

    return average_clock_difference

def synchronizeAllClocks():
    while True:
        try:
            print("\nNew synchronization cycle started.")
            if len(client_data) > 0:
                average_clock_difference = calculateAvgDifference()
                for client_addr, client in list(client_data.items()):
                    try:
                        synchronize_time = datetime.datetime.now() + average_clock_difference
                        client['connector'].send(str(synchronize_time).encode())
                    except Exception as e:
                        print(f"Unable to send time to {client_addr}. Removing client.")
                        del client_data[client_addr]
            else:
                print("No clients connected. Synchronization not applicable.")

            time.sleep(5)
            
        except Exception as e:
            print("Error during synchronization:", e)
            break

def initiateClockServer(port=8080):
    master_server = socket.socket()
    master_server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    master_server.settimeout(1)  # Add timeout to accept

    print("Socket at master node created successfully\n")
    master_server.bind(('', port))

    master_server.listen(10)
    print("Clock server started...\n")

    try:
        print("Starting to make connections...\n")
        master_thread = threading.Thread(
            target=startConnecting,
            args=(master_server,)
        )
        master_thread.start()

        print("Starting synchronization parallelly...\n")
        sync_thread = threading.Thread(
            target=synchronizeAllClocks,
            args=()
        )
        sync_thread.start()

        master_thread.join()
        sync_thread.join()

    except KeyboardInterrupt:
        print("\n[Server Shutdown] Keyboard interrupt received. Closing server socket.")
        master_server.close()

if __name__ == "__main__":
    initiateClockServer(8080)
