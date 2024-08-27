import axios from "axios";

export default async function searchFoodTrucks(
  query: string, 
  lat: number, 
  lon: number,
  distance: number, 
  pageNo: number, 
  pageSize: number
): Promise<any> {
    const { data } = await axios.get("/api/search-food-truck", {
      params: {
        query,
        lat,
        lon,
        distance,
        pageNo,
        pageSize
      }
    })
    console.log(data);
    return data;
}
