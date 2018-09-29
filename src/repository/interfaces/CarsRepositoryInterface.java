package repository.interfaces;

import javafx.collections.ObservableList;
import model.interfaces.CarInterface;

public interface CarsRepositoryInterface {
    public boolean addNewCar(CarInterface car);
    public boolean updateCar(CarInterface car);
    public boolean deleteCar(CarInterface car);
    public ObservableList<CarInterface> getAllCars();
}
