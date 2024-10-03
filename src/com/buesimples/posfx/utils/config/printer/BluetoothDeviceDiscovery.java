package com.buesimples.posfx.utils.config.printer;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.util.ArrayList;

public class BluetoothDeviceDiscovery {

   public static void discoverDevices() {
      final ArrayList<String> devices = new ArrayList<>();

      // Inicializa o LocalDevice
      try {
         LocalDevice localDevice = LocalDevice.getLocalDevice();
         System.out.println("Dispositivo local: " + localDevice.getFriendlyName());

         // Inicia a descoberta de dispositivos
         DiscoveryAgent agent = localDevice.getDiscoveryAgent();
         agent.startInquiry(DiscoveryAgent.GIAC, new DiscoveryListener() {
            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
               try {
                  String deviceName = btDevice.getFriendlyName(false);
                  String deviceAddress = btDevice.getBluetoothAddress();
                  devices.add(deviceAddress);
                  System.out.println("Dispositivo encontrado: " + deviceName + " [" + deviceAddress + "]");
               } catch (Exception e) {
                  System.err.println("Erro ao obter nome do dispositivo: " + e.getMessage());
               }
            }

            public void inquiryCompleted(int discType) {
               System.out.println("Descoberta de dispositivos concluída.");
               // Aqui você pode adicionar lógica para se conectar ao primeiro dispositivo
               // encontrado, por exemplo
               if (!devices.isEmpty()) {
                  connectToDevice(devices.get(0)); // Tente conectar ao primeiro dispositivo
               }
            }

            public void serviceSearchCompleted(int transID, int respCode) {
            }

            public void servicesDiscovered(int transID, ServiceRecord[] records) {
            }
         });

         // Aguarda a conclusão da descoberta
         synchronized (devices) {
            devices.wait(); // Bloqueia até que a descoberta esteja completa
         }

      } catch (Exception e) {
         System.err.println("Erro ao descobrir dispositivos Bluetooth: " + e.getMessage());
      }
   }

   private static void connectToDevice(String bluetoothAddress) {
      try {
         // Conectar ao dispositivo usando o endereço Bluetooth
         StreamConnection connection = (StreamConnection) Connector.open("btspp://" + bluetoothAddress);
         System.out.println("Conectado ao dispositivo: " + bluetoothAddress);
         // Aqui você pode adicionar lógica para enviar comandos de impressão, etc.
         connection.close();
      } catch (Exception e) {
         System.err.println("Erro ao conectar ao dispositivo: " + e.getMessage());
      }
   }
}
