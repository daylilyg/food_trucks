import { useState, useEffect } from "react";
import {
  MapContainer,
  Marker,
  Popup,
  TileLayer,
  LayerGroup,
  Circle
} from "react-leaflet";
import {
  type LatLng,
  Icon
} from 'leaflet';
import {
  Divider,
  Flex,
  Input,
  Space,
  Button,
  Select,
  List,
  Col,
  Row,
  Tag,
} from "antd";
import InfiniteScroll from "react-infinite-scroll-component";
import { EnvironmentTwoTone } from "@ant-design/icons";
import type { GetProps } from "antd";

import ChangeView from "../components/ChangeView";
import MapEventCallback from "../components/MapEventCallback";
import { type FoodTruck } from "../types/FoodTruck";
import searchFoodTrucks from "../api/FoodTruckQuery";

const { Search } = Input;

type SearchProps = GetProps<typeof Input.Search>;

import "leaflet/dist/leaflet.css";

const StatusColors = new Map<string, string>();
StatusColors.set("APPROVED", "green");
StatusColors.set("EXPIRED", "yellow");
StatusColors.set("ISSUED", "orange");
StatusColors.set("REQUESTED", "gray");
StatusColors.set("SUSPENDED", "red");

const RECALL_ICON = new Icon({ iconUrl: '/favicon.ico', iconSize: [12, 12], iconAnchor: [6, 6] });

export default function MainPage() {
  const [centerLat, setCenterLat] = useState(37.7878);
  const [centerLon, setCenterLon] = useState(-122.4);
  const [zoom, setZoom] = useState(15);
  const [radius, setRadius] = useState(1000);
  const [foodTrucks, setFoodTrucks] = useState<FoodTruck[]>([]);
  const [isSelected, setSelected] = useState(false);
  const [selectedIdx, setSelectedIdx] = useState(0);
  const [pageNo, setPageNo] = useState(0);
  const pageSize = 10;
  const [query, setQuery] = useState("");
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    loadMoreData();
  }, []);

  const greenOptions = { color: "green", fillColor: "green" };

  const refreshFoodTrucks = () => {
    setPageNo(0);
    setSelected(false);
    setFoodTrucks([]);
    loadMoreData();
  }

  const onSearch: SearchProps["onSearch"] = (value) => {
    console.log(value);
    setQuery(value);
    refreshFoodTrucks();
  };

  const handleRadiusChange = (value: string) => {
    console.log(`selected ${value}`);
    setRadius(Number.parseInt(value));
    refreshFoodTrucks();
  };

  const handleDragCenterUpdate = (center: LatLng) => {
    setCenterLat(center.lat);
    setCenterLon(center.lng);
    refreshFoodTrucks();
  }

  const handleLocation = () => {
    navigator.geolocation.getCurrentPosition((position) => {
      setCenterLat(position.coords.latitude);
      setCenterLon(position.coords.longitude);
      console.log(centerLat, centerLon);
    });
  };

  const loadMoreData = () => {
    const nextPage = searchFoodTrucks(
      query,
      centerLat,
      centerLon,
      radius,
      pageNo,
      pageSize
    );
    nextPage
      .then((p) => {
        const updatedList = [...foodTrucks, ...p.content];
        setFoodTrucks(updatedList);
        setTotalPages(p.page.totalPages);
        setPageNo(pageNo + 1);
      })
      .catch((e) => {
        console.log(e);
      });
  };

  const handleFoodTruckClick = (index: number) => {
    return () => {
      console.log(index);
      setSelectedIdx(index);
      setSelected(true);
    };
  };

  return (
    <div style={{ marginTop: 20 }}>
      <div>
        <Flex wrap justify="center" gap="small">
          <Space direction="horizontal">
            Find food truck
            <Search
              placeholder="input food truct info"
              onSearch={onSearch}
              enterButton
              style={{ width: 300 }}
            />
            <Button
              shape="circle"
              icon={<EnvironmentTwoTone />}
              onClick={handleLocation}
            ></Button>
            <Select
              defaultValue="1000"
              style={{ width: 120 }}
              onChange={handleRadiusChange}
              options={[
                { value: "500", label: "500m" },
                { value: "1000", label: "1km" },
                { value: "5000", label: "5km" },
                { value: "10000", label: "10km" },
              ]}
            />
          </Space>
        </Flex>
      </div>

      <Divider orientation="left" plain />

      <Row>
        <Col span={4} style={{ backgroundColor: "white" }}>
          <div
            id="scrollableDiv"
            style={{
              height: "1080px",
              maxHeight: "86vh",
              overflow: "auto",
              padding: "0 16px",
              border: "1px solid rgba(140, 140, 140, 0.35)",
            }}
          >
            <InfiniteScroll
              dataLength={foodTrucks.length}
              next={loadMoreData}
              hasMore={pageNo < totalPages}
              loader={<p>loading...</p>}
              endMessage={<Divider plain>It is all, nothing more 🤐</Divider>}
              scrollableTarget="scrollableDiv"
            >
              <List
                itemLayout="horizontal"
                dataSource={foodTrucks}
                renderItem={(item, index) => (
                  <List.Item
                    style={{ margin: 15 }}
                    onClick={handleFoodTruckClick(index)}
                  >
                    <List.Item.Meta
                      title={
                        <p>
                          {index + 1}. {item.applicant}
                        </p>
                      }
                      description={
                        <Flex gap="4px 0" wrap>
                          <Tag color={StatusColors.get(item.status)}>
                            {item.status}
                          </Tag>
                          <p>{item.foodItems}</p>
                        </Flex>
                      }
                    />
                  </List.Item>
                )}
              />
            </InfiniteScroll>
          </div>
        </Col>
        <Col span={20}>
          <MapContainer
            center={[centerLat, centerLon]}
            zoom={zoom}
            style={{
              height: "1080px",
              maxHeight: "86vh",
            }}
          >
            <LayerGroup>
              <Circle
                center={[centerLat, centerLon]}
                pathOptions={greenOptions}
                radius={radius}
              />
            </LayerGroup>
            <ChangeView center={[centerLat, centerLon]} zoom={zoom} />
            <MapEventCallback updateZoom={setZoom} updateCenter={handleDragCenterUpdate} />
            <TileLayer
              attribution="&copy; Food Truck Explorer"
              url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />

            {
              foodTrucks.map((ft, index) => 
                (<Marker
                  key={ft.locationId}
                  position={[
                    ft.latitude,
                    ft.longitude,
                  ]}
                  icon={RECALL_ICON}
                  eventHandlers={{
                    click: () => {
                      setSelectedIdx(index);
                      setSelected(true);
                    }
                  }}
                />)
              )
            }

            {isSelected && (
              <Marker
                key={foodTrucks[selectedIdx].locationId}
                position={[
                  foodTrucks[selectedIdx].latitude,
                  foodTrucks[selectedIdx].longitude,
                ]}
              >
                <Popup>
                  <div>
                    <h3>{foodTrucks[selectedIdx].applicant}</h3>
                    <Tag color={StatusColors.get(foodTrucks[selectedIdx].status)}>
                            {foodTrucks[selectedIdx].status}
                    </Tag>
                    <p>{foodTrucks[selectedIdx].foodItems}</p>
                  </div>
                </Popup>
              </Marker>
            )}
          </MapContainer>
        </Col>
      </Row>
    </div>
  );
}
