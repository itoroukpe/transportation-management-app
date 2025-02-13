@RestController
@RequestMapping("/vehicles") // Base path
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/")
    public String home() {
        return "Welcome to the Vehicle Management System";
    }

    @GetMapping("/all")
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }
}
