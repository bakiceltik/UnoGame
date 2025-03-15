# Duo Card Game

Duo Card Game, Java programlama dili ile geliştirilmiş bir kart oyunudur. Bu oyun Uno'ya benzer özelliklere sahiptir ve 2-4 oyuncu arasında oynanır.

## Proje Yapısı

Proje, katmanlı mimari prensiplerine uygun olarak tasarlanmıştır:

1. **Presentation Layer (Sunum Katmanı)**
   - Kullanıcıya oyunun akışını gösterir.
   - `ConsoleUI` sınıfı konsolda oyunu görüntüler.
   - `DuoCardGameMain` oyunun başlangıç noktasıdır.

2. **Application Layer (Uygulama Katmanı)**
   - Oyun akışını yönetir ve oyun kurallarını uygular.
   - `GameManager` sınıfı oyun akışını yönetir.
   - `TurnManager` sınıfı oyuncu sırasını ve yönünü belirler.

3. **Domain Layer (İş Mantığı Katmanı)**
   - Oyunun kurallarını yönetir, oyuncular ve kartları temsil eder.
   - `Card`, `Player`, `Deck` gibi temel sınıflar.
   - Mediator desenini kullanarak oyuncular arası iletişimi sağlar.

4. **Data Access Layer (Veri Katmanı)**
   - CSV dosyasına veri kaydetmek ve verileri almak.
   - `GameRepository` ve `CSVGameRepository` sınıfları.

## Tasarım Desenleri

Proje aşağıdaki tasarım desenlerini kullanmaktadır:

- **Mediator Pattern**: `GameMediator` ve `DuoGameMediator` sınıfları oyun elemanları arasındaki iletişimi yönetir.
- **Strategy Pattern**: Kart oynama stratejileri için kullanılır.
- **Factory Pattern**: Kart oluşturulması için kullanılır.

## Gereksinimler

- Java 8 veya üzeri
- Maven

## Kurulum ve Çalıştırma

### Maven ile Çalıştırma

1. Terminal veya komut istemcisini açın
2. Proje dizinine gidin
3. Projeyi derlemek için:
   ```
   mvn clean compile
   ```
4. Projeyi çalıştırmak için:
   ```
   mvn exec:java -Dexec.mainClass="com.duocardgame.presentation.DuoCardGameMain"
   ```

### JAR Dosyası Oluşturma

1. JAR dosyası oluşturmak için:
   ```
   mvn clean package
   ```
2. Oluşturulan JAR dosyasını çalıştırmak için:
   ```
   java -jar target/duocardgame-1.0.jar
   ```

## Oyun Kuralları

- Her oyuncu başlangıçta 7 kart alır.
- Amaç, elinizdeki tüm kartları bitirmektir.
- Üst kart ile aynı renk veya sayıdaki kartları oynayabilirsiniz.
- Oynayacak kartınız yoksa desteden kart çekmelisiniz.
- İlk elindeki kartları bitiren oyuncu turu kazanır.
- 500 puana ulaşan oyuncu oyunu kazanır.

### Özel Kartlar

- **Draw Two**: Sonraki oyuncu 2 kart çeker ve sırasını kaybeder.
- **Reverse**: Oyun yönünü değiştirir.
- **Skip**: Sonraki oyuncunun sırası atlanır.
- **Wild**: İstediğiniz rengi seçebilirsiniz.
- **Wild Draw Four**: Renk seçer ve sonraki oyuncu 4 kart çeker.
- **Shuffle Hands**: Tüm oyuncuların kartları karıştırılıp yeniden dağıtılır.

## Oyun Görselleri

Oyun konsol tabanlı bir arayüze sahiptir. ANSI renk kodları kullanılarak kartlar renkli olarak gösterilir.

## Geliştirme

Projeyi geliştirmek için:

1. Projeyi fork edin
2. Geliştirmelerinizi yapın
3. Pull request gönderin

## Lisans

Bu proje açık kaynaklıdır. 