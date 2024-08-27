import { useMap } from "react-leaflet";
import { type LatLngExpression } from "leaflet";

interface Props {
  center: LatLngExpression;
  zoom: number;
}

export default function ChangeView(props: Props) {
  const map = useMap();
  map.setView(props.center, props.zoom);
  return null;
}
