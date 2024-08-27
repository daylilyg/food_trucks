import { useMapEvents } from "react-leaflet";

interface Props {
  updateZoom: any,
  updateCenter: any
}

export default function MapEventCallback(props:Props) {
    const map = useMapEvents({
      zoomend() {
        console.log(map.getZoom());
        props.updateZoom(map.getZoom());
      },
      drag() {
        console.log(map.getCenter())
        props.updateCenter(map.getCenter());
      }
    })

    return null;
  }