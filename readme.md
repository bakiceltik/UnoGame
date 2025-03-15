# Duo Card Game

Bu proje, Duo Card Game adlı kart oyununu Java programlama dili ile gerçekleştirmektedir. Oyun, 2-4 oyuncunun yer aldığı, her turun sonunda kazanan oyuncunun diğer oyuncuların ellerinde kalan kartların değeri kadar puan kazandığı ve ilk 500 puana ulaşan oyuncunun oyunu kazandığı bir kart oyunudur.

## Proje Yapısı

Proje, 4 katmanlı mimari kullanılarak geliştirilmiştir:

1. **Presentation Layer (Sunum Katmanı)**:
   - Kullanıcı arayüzü bileşenleri
   - `ConsoleUI`: Konsol tabanlı kullanıcı arayüzü
   - `DuoCardGameMain`: Uygulamanın giriş noktası
   - Application Layer'a bağımlıdır

2. **Application Layer (Uygulama Katmanı)**:
   - Oyun akışını ve kurallarını yöneten sınıflar
   - `GameManager`: Oyun akışını koordine eden sınıf
   - `GameService`: Domain Layer ile iletişim kuran servis sınıfı
   - `TurnManager`: Oyuncu sırası ve turları yöneten sınıf
   - Domain Layer'a bağımlıdır

3. **Domain Layer (İş Mantığı Katmanı)**:
   - Oyun modelini ve kurallarını tanımlayan sınıflar
   - `Card`, `NumberCard`, `ActionCard`: Kart hiyerarşisi
   - `Player`: Oyuncu modeli
   - `Deck`: Kart destesi
   - `Color`, `CardType`: Enum sınıfları
   - `GameMediator`, `DuoGameMediator`: Mediator Pattern uygulaması
   - Data Access Layer'a bağımlıdır

4. **Data Access Layer (Veri Katmanı)**:
   - Veri erişim ve kalıcılık sınıfları
   - `GameRepository`: Oyun durumunu kaydetmek için arayüz
   - `CSVGameRepository`: CSV dosyasına veri kaydetme ve okuma işlevselliği
   - Hiçbir katmana bağımlı değildir

## Oyun Kuralları

- 2-4 oyuncu, daire şeklinde oturur
- 109 kartlık özel bir deste kullanılır
- Her oyuncuya başlangıçta 7 kart dağıtılır
- Amaç, elinizdeki tüm kartlardan ilk kurtulan olmaktır
- Her turun sonunda, kazanan oyuncu diğer oyuncuların elinde kalan kartların değeri kadar puan kazanır
- İlk 500 puana ulaşan oyuncu oyunu kazanır
- Oyun durumu her tur sonunda CSV dosyasına kaydedilir

## Kullanılan Tasarım Desenleri

- **Katmanlı Mimari (Layered Architecture)**: Presentation, Application, Domain ve Data Access katmanları
- **Mediator Pattern**: Oyun bileşenleri arasındaki iletişimi koordine etmek için `GameMediator` arayüzü
- **Repository Pattern**: Veri erişimi için `GameRepository` arayüzü
- **Object-Oriented Design Principles**: Inheritance, Polymorphism, Encapsulation, Abstraction

## Çalıştırma

Projeyi çalıştırmak için:

1. Kodu klonlayın
2. Maven kullanarak derleyin: `mvn clean install`
3. `DuoCardGameMain` sınıfını çalıştırın:
   ```
   java -cp target/duocardgame-1.0-SNAPSHOT.jar com.duocardgame.presentation.DuoCardGameMain
   ```

## Kart Tipleri ve Etkileri

- **Sayı Kartları (0-9)**: Yüz değeri puan değerini belirler
- **Draw Two (+2)**: Sonraki oyuncu 2 kart çeker ve turunu kaçırır
- **Reverse**: Oyun yönünü değiştirir
- **Skip**: Sonraki oyuncuyu atlar
- **Wild**: Oyuncu istediği rengi seçebilir
- **Wild Draw Four (+4)**: Sonraki oyuncu 4 kart çeker ve oyuncu renk seçer
- **Shuffle Hands**: Tüm oyuncuların ellerindeki kartlar karıştırılır ve yeniden dağıtılır 