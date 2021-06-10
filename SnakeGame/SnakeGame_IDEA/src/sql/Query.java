package sql;

import java.util.List;

/**
 * Работа с базой данных
 * @author Admin
 */
public interface Query<T> {
    /**
     * Получение всех строк из таблицы
     * @return список всех строк таблицы
     */
    public List<T> getAll();
    /**
     * Добавление строки в таблицу
     * @param t объект для добавления
     * @return true - в случае успешного добавления, иначе - false
     */
    public boolean add(T t);
    /**
     * Редактирование строки таблицы
     * @param t объект для редактирования
     * @return true - в случае успешного редактирования, иначе - false
     */
    public boolean update(T t);
    /**
     * Удаление строки из таблицы
     * @param id идентификатор строки
     * @return true - в случае успешного удаления, иначе - false
     */
    public boolean delete(int id);
}
